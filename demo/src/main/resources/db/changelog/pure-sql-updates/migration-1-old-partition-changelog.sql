CREATE OR REPLACE FUNCTION partition_for_animal()
RETURNS TRIGGER AS '
DECLARE v_partition_name text;
BEGIN
    IF (NEW.dtype = ''Feline'') OR (NEW.dtype = ''Bird'') THEN
        v_partition_name := format( ''animal_%s'', NEW.dtype);
    ELSE
        v_partition_name := ''animal_other'';
    END IF;

    EXECUTE ''INSERT INTO '' || v_partition_name || '' VALUES ( ($1).* )'' USING NEW;
    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION delete_parent_row()
RETURNS TRIGGER AS '
DECLARE
BEGIN
    DELETE FROM ONLY animal WHERE id = NEW.id;
    RETURN NULL;
END;
' LANGUAGE plpgsql;

CREATE TABLE animal_feline (like animal including all) inherits (animal);
ALTER TABLE animal_feline add constraint dtype_check check (dtype = 'Feline');

CREATE TABLE animal_bird (like animal including all) inherits (animal);
ALTER TABLE animal_bird add constraint dtype_check check (dtype = 'Bird');

CREATE TABLE animal_other (like animal including all) inherits (animal);
ALTER TABLE animal_other add constraint dtype_check check (dtype not in ('Feline', 'Bird'));

CREATE TRIGGER partition_animal_trigger BEFORE INSERT ON animal FOR EACH ROW EXECUTE PROCEDURE partition_for_animal();
CREATE TRIGGER after_insert_row_trigger AFTER INSERT ON animal FOR EACH ROW EXECUTE PROCEDURE delete_parent_row();