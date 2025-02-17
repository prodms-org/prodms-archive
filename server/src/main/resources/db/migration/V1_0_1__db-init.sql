CREATE TABLE IF NOT EXISTS units (
    number              varchar(50)         NOT NULL    PRIMARY KEY,
    name                varchar(50)         NOT NULL,
    type                integer             NOT NULL,
    version             integer             NOT NULL,
    status              integer             NOT NULL,
    created_at          bigserial           NOT NULL,
    updated_at          bigserial           NOT NULL,
    additional          varchar(1000),
    is_active           boolean             NOT NULL    DEFAULT true
);

CREATE TABLE IF NOT EXISTS units_history (
    number              varchar(50)         NOT NULL,
    version             integer             NOT NULL,
    operation           integer             NOT NULL,
    json                varchar(1500)       NOT NULL,
    PRIMARY KEY(number, version)
);













