-- 切换默认 OSS 为 MinIO（对象存储）
UPDATE sys_oss_config SET status = '1', update_time = sysdate() WHERE config_key <> 'minio';
UPDATE sys_oss_config
SET status = '0',
    access_key = 'ruoyi',
    secret_key = 'ruoyi123',
    bucket_name = 'ruoyi',
    endpoint = '127.0.0.1:9000',
    domain = '',
    is_https = 'N',
    access_policy = '1',
    update_time = sysdate()
WHERE config_key = 'minio';
