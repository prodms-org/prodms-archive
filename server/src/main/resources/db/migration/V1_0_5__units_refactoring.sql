-- Added parent constraint (need for getting drawing for -01; -02; e.g.)
ALTER TABLE units
    ADD COLUMN parent_number;
ALTER TABLE units
    ADD CONSTRAINT fk_unit_parent
        FOREIGN KEY (parent_number) units(number);