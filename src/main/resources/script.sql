create table biller_category
(
    id                bigint not null
        constraint biller_category_pkey
            primary key,
    billercategory_id bigint,
    biller_category   varchar(255)
);

alter table biller_category
    owner to postgres;

create table biller
(
    id                 bigint                not null
        constraint biller_pkey
            primary key,
    biller_id          varchar(255),
    biller             varchar(255),
    address            varchar(255),
    phone_number       varchar(255),
    biller_category_id bigint
        constraint fk_biller_biller_category_id
            references biller_category,
    is_popular         boolean default false not null
);

alter table biller
    owner to postgres;

create table biller_platform
(
    id                bigint not null
        constraint biller_platform_pkey
            primary key,
    billerplatform_id bigint,
    biller_platform   varchar(255),
    amount            double precision,
    biller_id         bigint
        constraint fk_biller_platform_biller_id
            references biller,
    value_id          varchar(255),
    value_name        varchar(255)
);

alter table biller_platform
    owner to postgres;

create table countrol_account
(
    id                    bigint not null
        constraint countrol_account_pkey
            primary key,
    countrol_account_code varchar(255),
    countrol_account_name varchar(255)
);

alter table countrol_account
    owner to postgres;

create table databasechangelog
(
    id            varchar(255) not null,
    author        varchar(255) not null,
    filename      varchar(255) not null,
    dateexecuted  timestamp    not null,
    orderexecuted integer      not null,
    exectype      varchar(10)  not null,
    md5sum        varchar(35),
    description   varchar(255),
    comments      varchar(255),
    tag           varchar(255),
    liquibase     varchar(20),
    contexts      varchar(255),
    labels        varchar(255),
    deployment_id varchar(10)
);

alter table databasechangelog
    owner to postgres;

create table double_entry_logger
(
    id                bigint not null
        constraint double_entry_logger_pkey
            primary key,
    date_entered      date,
    double_entry_code varchar(255),
    amount            double precision,
    narration         varchar(255),
    debit_id          bigint
        constraint fk_double_entry_logger_debit_id
            references countrol_account,
    credit_id         bigint
        constraint fk_double_entry_logger_credit_id
            references countrol_account
);

alter table double_entry_logger
    owner to postgres;

create table jhi_authority
(
    name varchar(50) not null
        constraint jhi_authority_pkey
            primary key
);

alter table jhi_authority
    owner to postgres;

create table jhi_persistent_audit_event
(
    event_id   bigint      not null
        constraint jhi_persistent_audit_event_pkey
            primary key,
    principal  varchar(50) not null,
    event_date timestamp,
    event_type varchar(255)
);

alter table jhi_persistent_audit_event
    owner to postgres;

create index idx_persistent_audit_event
    on jhi_persistent_audit_event (principal, event_date);

create table jhi_persistent_audit_evt_data
(
    event_id bigint       not null
        constraint fk_evt_pers_audit_evt_data
            references jhi_persistent_audit_event,
    name     varchar(150) not null,
    value    varchar(255),
    constraint jhi_persistent_audit_evt_data_pkey
        primary key (event_id, name)
);

alter table jhi_persistent_audit_evt_data
    owner to postgres;

create index idx_persistent_audit_evt_data
    on jhi_persistent_audit_evt_data (event_id);

create table jhi_user
(
    id                 bigint      not null
        constraint jhi_user_pkey
            primary key,
    login              varchar(50) not null
        constraint ux_user_login
            unique,
    password_hash      varchar(60) not null,
    first_name         varchar(50),
    last_name          varchar(50),
    email              varchar(191),
    image_url          varchar(256),
    activated          boolean     not null,
    lang_key           varchar(10),
    activation_key     varchar(20),
    reset_key          varchar(20),
    created_by         varchar(50) not null,
    created_date       timestamp,
    reset_date         timestamp,
    last_modified_by   varchar(50),
    last_modified_date timestamp
);

alter table jhi_user
    owner to postgres;

create table jhi_user_authority
(
    user_id        bigint      not null
        constraint fk_user_id
            references jhi_user,
    authority_name varchar(50) not null
        constraint fk_authority_name
            references jhi_authority,
    constraint jhi_user_authority_pkey
        primary key (user_id, authority_name)
);

alter table jhi_user_authority
    owner to postgres;

create table jounal
(
    id           bigint generated by default as identity
        constraint "jounalPK"
            primary key,
    memo         varchar(255),
    reference    varchar(255),
    trans_date   date,
    due_date     date,
    payment_type varchar(255),
    changes      double precision
);

alter table jounal
    owner to postgres;

create table kyclevel
(
    id                       bigint not null
        constraint kyclevel_pkey
            primary key,
    kyc_id                   bigint,
    kyc                      varchar(255),
    description              varchar(255),
    kyc_level                integer,
    phone_number             boolean,
    email_address            boolean,
    first_name               boolean,
    last_name                boolean,
    gender                   boolean,
    dateof_birth             boolean,
    address                  boolean,
    photo_upload             boolean,
    verified_bvn             boolean,
    verified_valid_id        boolean,
    evidenceof_address       boolean,
    verificationof_address   boolean,
    employment_details       boolean,
    daily_transaction_limit  double precision,
    cumulative_balance_limit double precision,
    payment_transaction      boolean,
    biller_transaction       boolean
);

alter table kyclevel
    owner to postgres;

create table profile_type
(
    id             bigint not null
        constraint profile_type_pkey
            primary key,
    profiletype_id bigint,
    profiletype    varchar(255)
);

alter table profile_type
    owner to postgres;

create table profile
(
    id                        bigint not null
        constraint profile_pkey
            primary key,
    profile_id                varchar(255),
    phone_number              varchar(255),
    gender                    varchar(255),
    date_of_birth             date,
    address                   varchar(255),
    photo                     bytea,
    photo_content_type        varchar(255),
    bvn                       varchar(255),
    valid_id                  varchar(255),
    user_id                   bigint
        constraint fk_profile_user_id
            references jhi_user,
    profile_type_id           bigint
        constraint fk_profile_profile_type_id
            references profile_type,
    kyc_id                    bigint
        constraint fk_profile_kyc_id
            references kyclevel,
    device_notification_token varchar,
    xxxx                      varchar(100)
);

alter table profile
    owner to postgres;

create table address
(
    id               bigint not null
        constraint address_pkey
            primary key,
    state            varchar(255),
    local_govt       varchar(255),
    latitude         double precision,
    longitude        double precision,
    address          varchar(255),
    address_owner_id bigint
        constraint fk_address_address_owner_id
            references profile
);

alter table address
    owner to postgres;

create table biller_transaction
(
    id              bigint not null
        constraint biller_transaction_pkey
            primary key,
    billertrans_id  bigint,
    transaction_ref varchar(255),
    narration       varchar(255),
    amount          double precision,
    phone_number_id bigint
        constraint fk_biller_transaction_phone_number_id
            references profile
);

alter table biller_transaction
    owner to postgres;

create table customersubscription
(
    id              bigint not null
        constraint customersubscription_pkey
            primary key,
    unique_id       varchar(255),
    frequency       varchar(255),
    phone_number_id bigint
        constraint fk_customersubscription_phone_number_id
            references profile,
    biller_id       bigint
        constraint fk_customersubscription_biller_id
            references biller
);

alter table customersubscription
    owner to postgres;

create table payment_transaction
(
    id                            bigint         not null
        constraint payment_transaction_pkey
            primary key,
    paymenttrans_id               bigint,
    transaction_type              varchar(255),
    transaction_ref               varchar(255),
    amount                        numeric(21, 2) not null,
    channel                       varchar(255)   not null,
    currency                      varchar(255)   not null,
    source_account                varchar(255)   not null,
    source_account_bank_code      varchar(255)   not null,
    source_account_name           varchar(255),
    source_narration              varchar(255)   not null,
    destination_account           varchar(255)   not null,
    destination_account_bank_code varchar(255)   not null,
    destination_account_name      varchar(255),
    destination_narration         varchar(255)   not null,
    transaction_owner_id          bigint
        constraint fk_payment_transaction_transaction_owner_id
            references profile,
    transaction_date              date
);

alter table payment_transaction
    owner to postgres;

create table scheme_category
(
    id                bigint not null
        constraint scheme_category_pkey
            primary key,
    schemecategory_id bigint,
    scheme_category   varchar(255),
    description       varchar(255)
);

alter table scheme_category
    owner to postgres;

create table scheme
(
    id                 bigint not null
        constraint scheme_pkey
            primary key,
    scheme_id          bigint,
    scheme             varchar(255),
    scheme_category_id bigint
        constraint fk_scheme_scheme_category_id
            references scheme_category
);

alter table scheme
    owner to postgres;

create table wallet_account_type
(
    id                  bigint not null
        constraint wallet_account_type_pkey
            primary key,
    accountype_id       bigint,
    wallet_account_type varchar(255)
);

alter table wallet_account_type
    owner to postgres;

create table wallet_account
(
    id                     bigint not null
        constraint wallet_account_pkey
            primary key,
    account_number         bigint,
    current_balance        double precision,
    date_opened            date,
    scheme_id              bigint
        constraint fk_wallet_account_scheme_id
            references scheme,
    wallet_account_type_id bigint
        constraint fk_wallet_account_wallet_account_type_id
            references wallet_account_type,
    account_owner_id       bigint
        constraint fk_wallet_account_account_owner_id
            references profile,
    account_name           varchar(255)
);

alter table wallet_account
    owner to postgres;

create table jounal_line
(
    id                bigint generated by default as identity
        constraint "jounal_linePK"
            primary key,
    credit            double precision default 0.00,
    debit             double precision default 0.00,
    jounal_id         bigint
        constraint "FKq1qkwi3bp726yehudbih8xe5b"
            references jounal,
    wallet_account_id bigint
        constraint "FK5xoglbb34pk9yi4m6ldwcnf33"
            references wallet_account
);

alter table jounal_line
    owner to postgres;

create table databasechangeloglock
(
    id          integer not null
        constraint databasechangeloglock_pkey
            primary key,
    locked      boolean not null,
    lockgranted timestamp,
    lockedby    varchar(255)
);

alter table databasechangeloglock
    owner to postgres;

create table biller_service_option
(
    id                 bigint not null
        constraint biller_service_option_pk
            primary key,
    column_name        varchar(255),
    column_type        varchar(255),
    column_length      varchar(255),
    required           boolean,
    has_price          boolean,
    fixed_amount       double precision,
    biller_platform_id bigint
        constraint biller_service_option_biller_platform_id_fk
            references biller_platform
);

alter table biller_service_option
    owner to postgres;

create unique index biller_service_option_id_uindex
    on biller_service_option (id);


