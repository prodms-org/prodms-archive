ALTER TABLE units_history
    ADD CONSTRAINT fk_unit_number FOREIGN KEY (number)
        REFERENCES units (number);