create table if not exists forms
(
    form_id     uuid primary key,
    code        varchar(32) unique not null,
    title       varchar(255)       not null,
    description text,
    fields      jsonb              not null default '[]',
    rules       jsonb                       default '[]',
    settings    jsonb              not null default '{}',
    status      varchar(20)                 default 'DRAFT',
    short_id    varchar(16) unique not null,
    owner_id    uuid,
    created_at  timestamp                   default current_timestamp,
    created_by  uuid,
    updated_at  timestamp,
    updated_by  uuid,
    deleted_at  timestamp
);
comment on table forms is '表单主表';
comment on column forms.form_id is '表单id';
comment on column forms.code is '表单编码';
comment on column forms.title is '标题';
comment on column forms.description is '描述';
comment on column forms.fields is '字段定义数组';
comment on column forms.rules is '跳题规则';
comment on column forms.settings is '表单设置';
comment on column forms.status is '状态';
comment on column forms.short_id is '短id，记录提交数据表';
comment on column forms.owner_id is '拥有者';
comment on column forms.created_at is '创建时间';
comment on column forms.updated_at is '最近修改时间';
comment on column forms.updated_by is '最近修改人';
comment on column forms.deleted_at is '删除时间';

create index if not exists idx_forms_updated_at on forms (updated_at);
create index if not exists idx_forms_deleted_at on forms (deleted_at) where deleted_at is null;


-- 表单版本表
create table if not exists form_versions
(
    form_version_id uuid primary key default gen_random_uuid(),
    form_id         uuid not null,
    form_version    int  not null,
    form_content    jsonb,
    published_at    timestamp        default current_timestamp,
    published_by    uuid,
    published_ip    inet,
    constraint uk_form_versions_form_form_version unique (form_id, form_version)
);
comment on table form_versions is '表单版本表';
comment on column form_versions.form_version_id is '主键';
comment on column form_versions.form_id is '表单id';
comment on column form_versions.form_version is '表单版本';
comment on column form_versions.form_content is '表单内容快照';
comment on column form_versions.published_at is '发布时间';
comment on column form_versions.published_by is '发布人';
comment on column form_versions.published_ip is '发布ip';


-- 提交数据表
create table if not exists submissions
(
    submission_id   uuid primary key,
    form_id         uuid,
    form_version    int,
    content         jsonb,
    submitted_at    timestamp default current_timestamp,
    submitted_ip    inet,
    duration_second int,
    created_at      timestamp default current_timestamp,
    updated_at      timestamp,
    deleted_at      timestamp
);
comment on table submissions is '提交数据表';
comment on column submissions.submission_id is '数据Id';
comment on column submissions.form_id is '表单Id';
comment on column submissions.form_version is '表单版本';
comment on column submissions.content is '原始内容';
comment on column submissions.submitted_at is '提交时间';
comment on column submissions.submitted_ip is '提交Ip';
comment on column submissions.duration_second is '填写时长（秒）';
comment on column submissions.created_at is '创建时间';
comment on column submissions.updated_at is '更新时间';
comment on column submissions.deleted_at is '删除时间';

create index if not exists idx_submissions_form_deleted_submitted on submissions (form_id, deleted_at, submitted_at);
create index if not exists idx_submissions_deleted on submissions (deleted_at) where deleted_at is null;

create table if not exists form_collaborators
(
    id         uuid primary key default gen_random_uuid(),
    form_id    uuid not null,
    user_id    uuid not null,
    role_code  varchar(32),
    created_at timestamp        default current_timestamp,
    updated_at timestamp        default current_timestamp,
    deleted_at timestamp,
    constraint uk_form_collaborators_form_user unique (form_id, user_id)
);
comment on table form_collaborators is '表单协作者';
comment on column form_collaborators.id is '主键';
comment on column form_collaborators.form_id is '表单Id';
comment on column form_collaborators.user_id is '用户Id';
comment on column form_collaborators.role_code is '角色编码';
comment on column form_collaborators.created_at is '创建时间';
comment on column form_collaborators.updated_at is '更新时间';

create index if not exists idx_form_collaborators_user_id on form_collaborators (user_id);
create index idx_form_collaborators_deleted_at on form_collaborators (deleted_at) where deleted_at is null;

create table if not exists form_roles
(
    form_role_id          uuid primary key default gen_random_uuid(),
    form_id               uuid        not null,
    role_code             varchar(32) not null,
    name                  varchar(32),
    description           varchar(255),
    operation_permissions jsonb,
    access_permissions    jsonb,
    created_at            timestamp        default current_timestamp,
    updated_at            timestamp        default current_timestamp,
    deleted_at            timestamp,
    constraint uk_form_roles_form_role unique (form_id, role_code)
);
comment on table form_roles is '表单角色';
comment on column form_roles.form_id is '表单id';
comment on column form_roles.role_code is '角色编码';
comment on column form_roles.name is '角色名称';
comment on column form_roles.description is '角色描述';
comment on column form_roles.operation_permissions is '操作权限';
comment on column form_roles.access_permissions is '访问权限';
comment on column form_roles.created_at is '创建时间';
comment on column form_roles.updated_at is '更新时间';

create index idx_form_roles_deleted_at on form_roles (deleted_at) where deleted_at is null;
