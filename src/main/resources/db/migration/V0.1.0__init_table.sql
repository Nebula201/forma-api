create table forms
(
    form_id     uuid primary key,
    code        varchar(32)  not null,
    title       varchar(255) not null,
    description text,
    fields      jsonb       default '[]',
    rules       jsonb       default '[]',
    settings    jsonb       default '{}',
    status      varchar(20) default 'DRAFT',
    created_by  varchar(50),
    created_at  timestamptz default current_timestamp,
    updated_by  varchar(50),
    updated_at  timestamptz default current_timestamp,
    updated_ip  inet,
    deleted_at  timestamptz,
    version     int
);
create unique index uk_forms_code on forms (code) where deleted_at is null;
create index idx_forms_updated_at on forms (updated_at);

comment on table forms is '表单主表';
comment on column forms.form_id is '表单id';
comment on column forms.code is '表单编码';
comment on column forms.title is '标题';
comment on column forms.description is '描述';
comment on column forms.fields is '字段定义数组';
comment on column forms.rules is '跳题规则';
comment on column forms.settings is '表单设置';
comment on column forms.status is '状态';
comment on column forms.created_by is '创建人ID';
comment on column forms.created_at is '创建时间';
comment on column forms.updated_by is '最后修改人ID';
comment on column forms.updated_at is '最后修改时间';
comment on column forms.updated_ip is '最后修改IP';
comment on column forms.deleted_at is '软删除标记';
comment on column forms.version is '乐观锁';

-- 表单版本表
create table form_versions
(
    form_version_id uuid primary key default gen_random_uuid(),
    form_id         uuid not null,
    form_version    int  not null,
    form_content    jsonb,
    published_at    timestamptz      default current_timestamp,
    published_by    varchar(50),
    published_ip    inet,
    created_by      varchar(50),
    created_at      timestamptz      default current_timestamp,
    updated_by      varchar(50),
    updated_at      timestamptz      default current_timestamp,
    updated_ip      inet,
    deleted_at      timestamptz,
    version         int              default 0
);
create unique index uk_form_versions_form_form_version on form_versions (form_id, form_version) where deleted_at is null;

comment on table form_versions is '表单版本表';
comment on column form_versions.form_version_id is '主键';
comment on column form_versions.form_id is '表单id';
comment on column form_versions.form_version is '表单版本';
comment on column form_versions.form_content is '表单内容快照';
comment on column form_versions.published_at is '发布时间';
comment on column form_versions.published_by is '发布人';
comment on column form_versions.published_ip is '发布IP';
comment on column form_versions.created_by is '创建人ID';
comment on column form_versions.created_at is '创建时间';
comment on column form_versions.updated_by is '最后修改人ID';
comment on column form_versions.updated_at is '最后修改时间';
comment on column form_versions.updated_ip is '最后修改IP';
comment on column form_versions.deleted_at is '软删除标记';
comment on column form_versions.version is '乐观锁';


-- 提交数据表
create table form_submissions
(
    form_id         uuid  not null,
    submission_no   int   not null,
    form_version    int   not null,
    content         jsonb not null,
    submitted_at    timestamptz default current_timestamp,
    submitted_ip    inet,
    duration_second int,
    created_by      varchar(50),
    created_at      timestamptz default current_timestamp,
    updated_by      varchar(50),
    updated_at      timestamptz default current_timestamp,
    updated_ip      inet,
    deleted_at      timestamptz,
    version         int         default 0,
    tracking_id     varchar(64),
    ua              varchar(255),
    os              varchar(50),
    referrer        varchar(64),
    device_type     varchar(16),
    device_hash     varchar(32),
    ip_country      varchar(50),
    ip_province     varchar(50),
    ip_city         varchar(50),
    attributes      jsonb,
    primary key (form_id, submission_no)
) partition by hash (form_id);
create table form_submissions_p00 partition of form_submissions for values with (modulus 4, remainder 0);
create table form_submissions_p01 partition of form_submissions for values with (modulus 4, remainder 1);
create table form_submissions_p02 partition of form_submissions for values with (modulus 4, remainder 2);
create table form_submissions_p03 partition of form_submissions for values with (modulus 4, remainder 3);

create index idx_submissions_form_submitted on form_submissions (form_id, submitted_at desc) where deleted_at is null;

comment on table form_submissions is '表单提交表';
comment on column form_submissions.form_id is '表单Id';
comment on column form_submissions.submission_no is '提交编号';
comment on column form_submissions.form_version is '表单版本';
comment on column form_submissions.content is '原始内容';
comment on column form_submissions.submitted_at is '提交时间';
comment on column form_submissions.submitted_ip is '提交Ip';
comment on column form_submissions.duration_second is '填写时长（秒）';
comment on column form_submissions.created_by is '创建人ID';
comment on column form_submissions.created_at is '创建时间';
comment on column form_submissions.updated_by is '最后修改人ID';
comment on column form_submissions.updated_at is '最后修改时间';
comment on column form_submissions.updated_ip is '最后修改IP';
comment on column form_submissions.deleted_at is '软删除标记';
comment on column form_submissions.version is '乐观锁';
comment on column form_submissions.tracking_id is '二维码追踪码';
comment on column form_submissions.ua is '浏览器 User-Agent';
comment on column form_submissions.os is '操作系统';
comment on column form_submissions.referrer is '来源渠道';
comment on column form_submissions.device_type is '设备类型 手机/桌面设备/平板';
comment on column form_submissions.device_hash is '设备指纹';
comment on column form_submissions.ip_country is '国家';
comment on column form_submissions.ip_province is '省份';
comment on column form_submissions.ip_city is '城市';
comment on column form_submissions.attributes is '额外属性';

create table form_submission_sequence
(
    form_id    uuid primary key,
    current_no bigint not null default 0
);
comment on table form_submission_sequence is '表单提交编号表';
comment on column form_submission_sequence.form_id is '表单Id';
comment on column form_submission_sequence.current_no is '当前编号';

create table form_collaborators
(
    id         uuid primary key default gen_random_uuid(),
    form_id    uuid        not null,
    user_id    varchar(50) not null,
    role_code  varchar(32),
    created_by varchar(50),
    created_at timestamptz      default current_timestamp,
    updated_by varchar(50),
    updated_at timestamptz      default current_timestamp,
    updated_ip inet,
    deleted_at timestamptz,
    version    int              default 0
);
create unique index uk_form_collaborators_active on form_collaborators (form_id, user_id) where deleted_at is null;
create index idx_form_collaborators_user_id on form_collaborators (user_id) where deleted_at is null;

comment on table form_collaborators is '表单协作者';
comment on column form_collaborators.id is '主键';
comment on column form_collaborators.form_id is '表单Id';
comment on column form_collaborators.user_id is '用户Id';
comment on column form_collaborators.role_code is '角色编码';
comment on column form_collaborators.created_by is '创建人ID';
comment on column form_collaborators.created_at is '创建时间';
comment on column form_collaborators.updated_by is '最后修改人ID';
comment on column form_collaborators.updated_at is '最后修改时间';
comment on column form_collaborators.updated_ip is '最后修改IP';
comment on column form_collaborators.deleted_at is '软删除标记';
comment on column form_collaborators.version is '乐观锁';

create table form_roles
(
    form_role_id          uuid primary key default gen_random_uuid(),
    form_id               uuid        not null,
    role_code             varchar(32) not null,
    name                  varchar(32),
    description           varchar(255),
    operation_permissions jsonb,
    access_permissions    jsonb,
    created_by            varchar(50),
    created_at            timestamptz      default current_timestamp,
    updated_by            varchar(50),
    updated_at            timestamptz      default current_timestamp,
    updated_ip            inet,
    deleted_at            timestamptz,
    version               int              default 0
);
create unique index uk_form_roles_form_role on form_roles (form_id, role_code) where deleted_at is null;

comment on table form_roles is '表单角色';
comment on column form_roles.form_id is '表单id';
comment on column form_roles.role_code is '角色编码';
comment on column form_roles.name is '角色名称';
comment on column form_roles.description is '角色描述';
comment on column form_roles.operation_permissions is '操作权限';
comment on column form_roles.access_permissions is '访问权限';
comment on column form_roles.created_by is '创建人ID';
comment on column form_roles.created_at is '创建时间';
comment on column form_roles.updated_by is '最后修改人ID';
comment on column form_roles.updated_at is '最后修改时间';
comment on column form_roles.updated_ip is '最后修改IP';
comment on column form_roles.deleted_at is '软删除标记';
comment on column form_roles.version is '乐观锁';

