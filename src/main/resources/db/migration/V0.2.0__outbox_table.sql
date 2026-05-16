create table if not exists outbox_events
(
    event_id      uuid primary key     default gen_random_uuid(),
    event_type    varchar(64) not null,
    event_version int         not null default 1,
    event_at      timestamp   not null,
    payload       jsonb       not null,
    status        varchar(16) not null default 'PENDING',
    published_at  timestamp,
    max_retries   int         not null default 3,
    retry_count   int         not null default 0,
    failed_reason varchar(255)
);

comment on table outbox_events is '事件发件箱';
comment on column outbox_events.event_id is '事件ID';
comment on column outbox_events.event_type is '事件类型';
comment on column outbox_events.event_version is '事件版本';
comment on column outbox_events.event_at is '事件发生时间';
comment on column outbox_events.payload is '事件内容';
comment on column outbox_events.status is '状态: PENDING/SENT/FAILED';
comment on column outbox_events.published_at is '发布时间';
comment on column outbox_events.max_retries is '最大重试次数';
comment on column outbox_events.retry_count is '已重试次数';
comment on column outbox_events.failed_reason is '失败原因';

create index idx_outbox_status_event_at on outbox_events (status, event_at) where status = 'PENDING';
