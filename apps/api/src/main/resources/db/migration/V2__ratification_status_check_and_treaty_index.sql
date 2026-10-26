ALTER TABLE ratification
    ADD CONSTRAINT ratification_status_check
    CHECK (status IN ('STATE_PARTY', 'SIGNATORY', 'NO_ACTION'));

CREATE INDEX idx_ratification_treaty_code ON ratification (treaty_code);
