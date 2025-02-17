CREATE TABLE IF NOT EXISTS rates (
    assembly_number            varchar(50)         NOT NULL,
    unit_number                varchar(50)         NOT NULL,
    rate                       integer             NOT NULL,
    version                    integer             NOT NULL,
    created_at                 bigserial           NOT NULL,
    updated_at                 bigserial           NOT NULL,
    is_active                  boolean             NOT NULL    DEFAULT true,
    PRIMARY KEY (assembly_number, unit_number),
    FOREIGN KEY (assembly_number) REFERENCES units (number),
    FOREIGN KEY (unit_number) REFERENCES units (number)
);