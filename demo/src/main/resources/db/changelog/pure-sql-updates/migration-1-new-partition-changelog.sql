CREATE TABLE animal_feline PARTITION OF animal FOR VALUES IN ('Feline');
CREATE TABLE animal_bird PARTITION OF animal FOR VALUES IN ('Bird');
CREATE TABLE animal_other PARTITION OF animal DEFAULT;