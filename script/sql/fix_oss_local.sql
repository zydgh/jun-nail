-- 切换默认 OSS 为本地磁盘存储（无需 MinIO）
UPDATE sys_oss_config SET status = '1', update_time = sysdate() WHERE config_key = 'minio';
UPDATE sys_oss_config SET status = '1', update_time = sysdate() WHERE config_key <> 'local';

INSERT INTO sys_oss_config (
    oss_config_id, tenant_id, config_key, access_key, secret_key, bucket_name, prefix,
    endpoint, domain, is_https, region, access_policy, status, ext1, create_dept, create_by, create_time, update_by, update_time, remark
)
SELECT 6, '000000', 'local', 'local', 'local', 'uploads', '',
       'local', '', 'N', '', '1', '0', '', 103, 1, sysdate(), 1, sysdate(), '本地磁盘存储，无需 MinIO'
WHERE NOT EXISTS (SELECT 1 FROM sys_oss_config WHERE config_key = 'local');

UPDATE sys_oss_config
SET status = '0',
    access_policy = '1',
    bucket_name = 'uploads',
    endpoint = 'local',
    access_key = 'local',
    secret_key = 'local',
    update_time = sysdate(),
    remark = '本地磁盘存储，无需 MinIO'
WHERE config_key = 'local';
