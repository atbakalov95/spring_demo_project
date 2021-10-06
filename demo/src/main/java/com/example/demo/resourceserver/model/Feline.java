package com.example.demo.resourceserver.model;

import com.example.demo.resourceserver.utils.NamedParamStatement;
import lombok.Data;
import lombok.SneakyThrows;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;
import java.util.Locale;

@Data
@Entity
public class Feline extends Animal{
    private String furType;

    @Override
    public List<String> getQueryInsertParameters() {
        List<String> parentList = super.getQueryInsertParameters();
        parentList.add("fur_type");
        return parentList;
    }

    @SneakyThrows
    public NamedParamStatement addBatch(NamedParamStatement namedParamStatement) {
        return namedParamStatement.setString("name", this.getName())
                .setInt("zoo_id", this.getZoo().getId())
                .setString("fur_type", this.getFurType())
                .setString("dtype", this.getClass().getSimpleName())
                .addBatch();
    }
}
