package org.dromara.common.oss.core;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.constant.Constants;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.file.FileUtils;
import org.dromara.common.oss.constant.OssConstant;
import org.dromara.common.oss.entity.UploadResult;
import org.dromara.common.oss.enums.AccessPolicyType;
import org.dromara.common.oss.exception.OssException;
import org.dromara.common.oss.properties.OssProperties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.BlockingInputStreamAsyncRequestBody;
import software.amazon.awssdk.core.async.ResponsePublisher;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.*;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * S3 存储协议 所有兼容S3协议的云厂商均支持
 * 阿里云 腾讯云 七牛云 minio
 * 当 configKey 为 local 时走本地磁盘存储
 *
 * @author AprilWind
 */
@Slf4j
public class OssClient {

    /**
     * 服务商
     */
    private final String configKey;

    /**
     * 配置属性
     */
    private final OssProperties properties;

    /**
     * 是否本地磁盘存储
     */
    private final boolean localMode;

    /**
     * 本地磁盘根目录
     */
    private final Path localRoot;

    /**
     * Amazon S3 异步客户端
     */
    private final S3AsyncClient client;

    /**
     * 用于管理 S3 数据传输的高级工具
     */
    private final S3TransferManager transferManager;

    /**
     * AWS S3 预签名 URL 的生成器
     */
    private final S3Presigner presigner;

    /**
     * 构造方法
     *
     * @param configKey     配置键
     * @param ossProperties Oss配置属性
     */
    public OssClient(String configKey, OssProperties ossProperties) {
        this.configKey = configKey;
        this.properties = ossProperties;
        this.localMode = isLocalConfig(configKey, ossProperties);
        try {
            if (this.localMode) {
                this.client = null;
                this.transferManager = null;
                this.presigner = null;
                String dirName = StringUtils.isNotBlank(properties.getBucketName()) ? properties.getBucketName() : "uploads";
                this.localRoot = Paths.get(System.getProperty("user.dir"), dirName).toAbsolutePath().normalize();
                Files.createDirectories(this.localRoot);
                log.info("本地OSS存储目录 => {}", this.localRoot);
                return;
            }
            this.localRoot = null;
            StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey()));

            boolean isStyle = !StringUtils.containsAny(properties.getEndpoint(), OssConstant.CLOUD_SERVICE);

            this.client = S3AsyncClient.builder()
                .credentialsProvider(credentialsProvider)
                .endpointOverride(URI.create(getEndpoint()))
                .region(of())
                .forcePathStyle(isStyle)
                .httpClient(NettyNioAsyncHttpClient.builder()
                    .connectionTimeout(Duration.ofSeconds(60))
                    .connectionAcquisitionTimeout(Duration.ofSeconds(30))
                    .maxConcurrency(100)
                    .maxPendingConnectionAcquires(1000)
                    .build())
                .build();

            this.transferManager = S3TransferManager.builder().s3Client(this.client).build();

            S3Configuration config = S3Configuration.builder().chunkedEncodingEnabled(false)
                .pathStyleAccessEnabled(isStyle).build();

            this.presigner = S3Presigner.builder()
                .region(of())
                .credentialsProvider(credentialsProvider)
                .endpointOverride(URI.create(getDomain()))
                .serviceConfiguration(config)
                .build();

        } catch (Exception e) {
            if (e instanceof OssException ossException) {
                throw ossException;
            }
            throw new OssException("配置错误! 请检查系统配置:[" + e.getMessage() + "]");
        }
    }

    private static boolean isLocalConfig(String configKey, OssProperties properties) {
        return OssConstant.LOCAL_CONFIG_KEY.equalsIgnoreCase(configKey)
            || OssConstant.LOCAL_CONFIG_KEY.equalsIgnoreCase(properties.getEndpoint());
    }

    /**
     * 上传文件到 Amazon S3，并返回上传结果
     *
     * @param filePath    本地文件路径
     * @param key         在 Amazon S3 中的对象键
     * @param md5Digest   本地文件的 MD5 哈希值（可选）
     * @param contentType 文件内容类型
     * @return UploadResult 包含上传后的文件信息
     * @throws OssException 如果上传失败，抛出自定义异常
     */
    public UploadResult upload(Path filePath, String key, String md5Digest, String contentType) {
        if (localMode) {
            try {
                Path target = resolveLocalPath(key);
                Files.createDirectories(target.getParent());
                Files.copy(filePath, target, StandardCopyOption.REPLACE_EXISTING);
                return UploadResult.builder().url(getUrl() + StringUtils.SLASH + key).filename(key).eTag(md5Digest).build();
            } catch (Exception e) {
                throw new OssException("上传文件失败，请检查配置信息:[" + e.getMessage() + "]");
            } finally {
                FileUtils.del(filePath);
            }
        }
        try {
            FileUpload fileUpload = transferManager.uploadFile(
                x -> {
                    x.source(filePath).putObjectRequest(
                        y -> y.bucket(properties.getBucketName())
                            .key(key)
                            .contentMD5(StringUtils.isNotEmpty(md5Digest) ? md5Digest : null)
                            .contentType(contentType)
                            .build()
                    );
                    if (log.isDebugEnabled()) {
                        x.addTransferListener(LoggingTransferListener.create());
                    }
                }
            );
            CompletedFileUpload uploadResult = fileUpload.completionFuture().join();
            String eTag = uploadResult.response().eTag();

            return UploadResult.builder().url(getUrl() + StringUtils.SLASH + key).filename(key).eTag(eTag).build();
        } catch (Exception e) {
            throw new OssException("上传文件失败，请检查配置信息:[" + e.getMessage() + "]");
        } finally {
            FileUtils.del(filePath);
        }
    }

    /**
     * 上传 InputStream 到 Amazon S3
     *
     * @param inputStream 要上传的输入流
     * @param key         在 Amazon S3 中的对象键
     * @param length      输入流的长度
     * @param contentType 文件内容类型
     * @return UploadResult 包含上传后的文件信息
     * @throws OssException 如果上传失败，抛出自定义异常
     */
    public UploadResult upload(InputStream inputStream, String key, Long length, String contentType) {
        if (!(inputStream instanceof ByteArrayInputStream)) {
            inputStream = new ByteArrayInputStream(IoUtil.readBytes(inputStream));
        }
        if (localMode) {
            try {
                Path target = resolveLocalPath(key);
                Files.createDirectories(target.getParent());
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
                return UploadResult.builder().url(getUrl() + StringUtils.SLASH + key).filename(key).build();
            } catch (Exception e) {
                throw new OssException("上传文件失败，请检查配置信息:[" + e.getMessage() + "]");
            }
        }
        try {
            BlockingInputStreamAsyncRequestBody body = BlockingInputStreamAsyncRequestBody.builder()
                .contentLength(length)
                .subscribeTimeout(Duration.ofSeconds(120))
                .build();

            Upload upload = transferManager.upload(
                x -> {
                    x.requestBody(body).putObjectRequest(
                        y -> y.bucket(properties.getBucketName())
                            .key(key)
                            .contentType(contentType)
                            .build()
                    );
                    if (log.isDebugEnabled()) {
                        x.addTransferListener(LoggingTransferListener.create());
                    }
                }
            );

            body.writeInputStream(inputStream);

            CompletedUpload uploadResult = upload.completionFuture().join();
            String eTag = uploadResult.response().eTag();

            return UploadResult.builder().url(getUrl() + StringUtils.SLASH + key).filename(key).eTag(eTag).build();
        } catch (Exception e) {
            throw new OssException("上传文件失败，请检查配置信息:[" + e.getMessage() + "]");
        }
    }

    /**
     * 下载文件从 Amazon S3 到临时目录
     *
     * @param path 文件在 Amazon S3 中的对象键
     * @return 下载后的文件在本地的临时路径
     * @throws OssException 如果下载失败，抛出自定义异常
     */
    public Path fileDownload(String path) {
        if (localMode) {
            try {
                Path source = resolveLocalPath(removeBaseUrl(path));
                Path tempFilePath = FileUtils.createTempFile().toPath();
                Files.copy(source, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
                return tempFilePath;
            } catch (Exception e) {
                throw new OssException("文件下载失败，错误信息:[" + e.getMessage() + "]");
            }
        }
        Path tempFilePath = FileUtils.createTempFile().toPath();
        FileDownload downloadFile = transferManager.downloadFile(
            x -> {
                x.destination(tempFilePath).getObjectRequest(
                    y -> y.bucket(properties.getBucketName())
                        .key(removeBaseUrl(path))
                        .build()
                );
                if (log.isDebugEnabled()) {
                    x.addTransferListener(LoggingTransferListener.create());
                }
            }
        );
        downloadFile.completionFuture().join();
        return tempFilePath;
    }

    /**
     * 下载文件从 Amazon S3 到 输出流
     *
     * @param key      文件在 Amazon S3 中的对象键
     * @param out      输出流
     * @param consumer 自定义处理逻辑
     * @throws OssException 如果下载失败，抛出自定义异常
     */
    public void download(String key, OutputStream out, Consumer<Long> consumer) {
        try {
            this.download(key, consumer).writeTo(out);
        } catch (Exception e) {
            throw new OssException("文件下载失败，错误信息:[" + e.getMessage() + "]");
        }
    }

    /**
     * 下载文件从 Amazon S3 到 输出流
     *
     * @param key                   文件在 Amazon S3 中的对象键
     * @param contentLengthConsumer 文件大小消费者函数
     * @return 写出订阅器
     * @throws OssException 如果下载失败，抛出自定义异常
     */
    public WriteOutSubscriber<OutputStream> download(String key, Consumer<Long> contentLengthConsumer) {
        if (localMode) {
            try {
                Path source = resolveLocalPath(key);
                long size = Files.size(source);
                Optional.ofNullable(contentLengthConsumer).ifPresent(c -> c.accept(size));
                return out -> {
                    try (InputStream in = Files.newInputStream(source)) {
                        IoUtil.copy(in, out);
                    }
                };
            } catch (Exception e) {
                throw new OssException("文件下载失败，错误信息:[" + e.getMessage() + "]");
            }
        }
        try {
            DownloadRequest.TypedBuilder<ResponsePublisher<GetObjectResponse>> typedBuilder = DownloadRequest.builder()
                .responseTransformer(AsyncResponseTransformer.toPublisher())
                .getObjectRequest(y -> y.bucket(properties.getBucketName()).key(key).build());
            if (log.isDebugEnabled()) {
                typedBuilder.addTransferListener(LoggingTransferListener.create());
            }

            Download<ResponsePublisher<GetObjectResponse>> publisherDownload = transferManager.download(typedBuilder.build());
            ResponsePublisher<GetObjectResponse> publisher = publisherDownload.completionFuture().join().result();
            Optional.ofNullable(contentLengthConsumer)
                .ifPresent(lengthConsumer -> lengthConsumer.accept(publisher.response().contentLength()));

            return out -> {
                try (WritableByteChannel channel = Channels.newChannel(out)) {
                    publisher.subscribe(byteBuffer -> {
                        while (byteBuffer.hasRemaining()) {
                            try {
                                channel.write(byteBuffer);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }).join();
                }
            };
        } catch (Exception e) {
            throw new OssException("文件下载失败，错误信息:[" + e.getMessage() + "]");
        }
    }

    /**
     * 删除云存储服务中指定路径下文件
     *
     * @param path 指定路径
     */
    public void delete(String path) {
        if (localMode) {
            try {
                Files.deleteIfExists(resolveLocalPath(removeBaseUrl(path)));
            } catch (Exception e) {
                throw new OssException("删除文件失败，请检查配置信息:[" + e.getMessage() + "]");
            }
            return;
        }
        try {
            client.deleteObject(
                x -> x.bucket(properties.getBucketName())
                    .key(removeBaseUrl(path))
                    .build());
        } catch (Exception e) {
            throw new OssException("删除文件失败，请检查配置信息:[" + e.getMessage() + "]");
        }
    }

    /**
     * 创建下载请求的预签名URL
     *
     * @param objectKey   对象KEY
     * @param expiredTime 链接授权到期时间
     */
    public String createPresignedGetUrl(String objectKey, Duration expiredTime) {
        if (localMode) {
            return getUrl() + StringUtils.SLASH + objectKey;
        }
        URL url = presigner.presignGetObject(
                x -> x.signatureDuration(expiredTime)
                    .getObjectRequest(
                        y -> y.bucket(properties.getBucketName())
                            .key(objectKey)
                            .build())
                    .build())
            .url();
        return url.toExternalForm();
    }

    /**
     * 创建上传请求的预签名URL
     *
     * @param objectKey   对象KEY
     * @param expiredTime 链接授权到期时间
     * @param metadata    元数据
     */
    public String createPresignedPutUrl(String objectKey, Duration expiredTime, Map<String, String> metadata) {
        if (localMode) {
            throw new OssException("本地存储不支持预签名上传");
        }
        URL url = presigner.presignPutObject(
                x -> x.signatureDuration(expiredTime)
                    .putObjectRequest(
                        y -> y.bucket(properties.getBucketName())
                            .key(objectKey)
                            .metadata(metadata)
                            .build())
                    .build())
            .url();
        return url.toExternalForm();
    }

    /**
     * 上传 byte[] 数据到 Amazon S3，使用指定的后缀构造对象键。
     *
     * @param data   要上传的 byte[] 数据
     * @param suffix 对象键的后缀
     * @return UploadResult 包含上传后的文件信息
     * @throws OssException 如果上传失败，抛出自定义异常
     */
    public UploadResult uploadSuffix(byte[] data, String suffix, String contentType) {
        return upload(new ByteArrayInputStream(data), getPath(properties.getPrefix(), suffix), Long.valueOf(data.length), contentType);
    }

    /**
     * 上传 InputStream 到 Amazon S3，使用指定的后缀构造对象键。
     *
     * @param inputStream 要上传的输入流
     * @param suffix      对象键的后缀
     * @param length      输入流的长度
     * @return UploadResult 包含上传后的文件信息
     * @throws OssException 如果上传失败，抛出自定义异常
     */
    public UploadResult uploadSuffix(InputStream inputStream, String suffix, Long length, String contentType) {
        return upload(inputStream, getPath(properties.getPrefix(), suffix), length, contentType);
    }

    /**
     * 上传文件到 Amazon S3，使用指定的后缀构造对象键
     *
     * @param file   要上传的文件
     * @param suffix 对象键的后缀
     * @return UploadResult 包含上传后的文件信息
     * @throws OssException 如果上传失败，抛出自定义异常
     */
    public UploadResult uploadSuffix(File file, String suffix) {
        return upload(file.toPath(), getPath(properties.getPrefix(), suffix), null, FileUtils.getMimeType(suffix));
    }

    /**
     * 获取文件输入流
     *
     * @param path 完整文件路径
     * @return 输入流
     */
    public InputStream getObjectContent(String path) throws IOException {
        Path tempFilePath = fileDownload(path);
        InputStream inputStream = Files.newInputStream(tempFilePath);
        FileUtils.del(tempFilePath);
        return inputStream;
    }

    /**
     * 获取 S3 客户端的终端点 URL
     *
     * @return 终端点 URL
     */
    public String getEndpoint() {
        String header = getIsHttps();
        return header + properties.getEndpoint();
    }

    /**
     * 获取 S3 客户端的终端点 URL（自定义域名）
     *
     * @return 终端点 URL
     */
    public String getDomain() {
        String domain = properties.getDomain();
        String endpoint = properties.getEndpoint();
        String header = getIsHttps();

        if (StringUtils.containsAny(endpoint, OssConstant.CLOUD_SERVICE)) {
            return StringUtils.isNotEmpty(domain) ? header + domain : header + endpoint;
        }

        if (StringUtils.isNotEmpty(domain)) {
            return domain.startsWith(Constants.HTTPS) || domain.startsWith(Constants.HTTP) ? domain : header + domain;
        }

        return header + endpoint;
    }

    /**
     * 根据传入的 region 参数返回相应的 AWS 区域
     * 如果 region 参数非空，使用 Region.of 方法创建并返回对应的 AWS 区域对象
     * 如果 region 参数为空，返回一个默认的 AWS 区域（例如，us-east-1），作为广泛支持的区域
     *
     * @return 对应的 AWS 区域对象，或者默认的广泛支持的区域（us-east-1）
     */
    public Region of() {
        String region = properties.getRegion();
        return StringUtils.isNotEmpty(region) ? Region.of(region) : Region.US_EAST_1;
    }

    /**
     * 获取云存储服务的URL
     *
     * @return 文件路径
     */
    public String getUrl() {
        if (localMode) {
            String domain = properties.getDomain();
            if (StringUtils.isNotEmpty(domain)) {
                return domain.startsWith(Constants.HTTPS) || domain.startsWith(Constants.HTTP)
                    || domain.startsWith(StringUtils.SLASH)
                    ? StringUtils.removeEnd(domain, StringUtils.SLASH)
                    : OssConstant.LOCAL_RESOURCE_PREFIX;
            }
            return OssConstant.LOCAL_RESOURCE_PREFIX;
        }
        String domain = properties.getDomain();
        String endpoint = properties.getEndpoint();
        String header = getIsHttps();
        if (StringUtils.containsAny(endpoint, OssConstant.CLOUD_SERVICE)) {
            return header + (StringUtils.isNotEmpty(domain) ? domain : properties.getBucketName() + "." + endpoint);
        }
        if (StringUtils.isNotEmpty(domain)) {
            return (domain.startsWith(Constants.HTTPS) || domain.startsWith(Constants.HTTP)) ?
                domain + StringUtils.SLASH + properties.getBucketName() : header + domain + StringUtils.SLASH + properties.getBucketName();
        }
        return header + endpoint + StringUtils.SLASH + properties.getBucketName();
    }

    /**
     * 生成一个符合特定规则的、唯一的文件路径。通过使用日期、UUID、前缀和后缀等元素的组合，确保了文件路径的独一无二性
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 文件路径
     */
    public String getPath(String prefix, String suffix) {
        String uuid = IdUtil.fastSimpleUUID();
        String datePath = DateUtils.datePath();
        String path = StringUtils.isNotEmpty(prefix) ?
            prefix + StringUtils.SLASH + datePath + StringUtils.SLASH + uuid : datePath + StringUtils.SLASH + uuid;
        return path + suffix;
    }

    /**
     * 移除路径中的基础URL部分，得到相对路径
     *
     * @param path 完整的路径，包括基础URL和相对路径
     * @return 去除基础URL后的相对路径
     */
    public String removeBaseUrl(String path) {
        return path.replace(getUrl() + StringUtils.SLASH, "");
    }

    /**
     * 服务商
     */
    public String getConfigKey() {
        return configKey;
    }

    /**
     * 是否本地磁盘存储
     */
    public boolean isLocalMode() {
        return localMode;
    }

    /**
     * 本地磁盘根目录
     */
    public Path getLocalRoot() {
        return localRoot;
    }

    /**
     * 获取是否使用 HTTPS 的配置，并返回相应的协议头部。
     *
     * @return 协议头部，根据是否使用 HTTPS 返回 "https://" 或 "http://"
     */
    public String getIsHttps() {
        return OssConstant.IS_HTTPS.equals(properties.getIsHttps()) ? Constants.HTTPS : Constants.HTTP;
    }

    /**
     * 检查配置是否相同
     */
    public boolean checkPropertiesSame(OssProperties properties) {
        return this.properties.equals(properties);
    }

    /**
     * 获取当前桶权限类型
     *
     * @return 当前桶权限类型code
     */
    public AccessPolicyType getAccessPolicy() {
        return AccessPolicyType.getByType(properties.getAccessPolicy());
    }

    private Path resolveLocalPath(String key) {
        Path resolved = localRoot.resolve(key).normalize();
        if (!resolved.startsWith(localRoot)) {
            throw new OssException("非法文件路径");
        }
        return resolved;
    }

}
