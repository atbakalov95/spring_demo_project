package com.example.demo.defaultapp.model;

import com.example.demo.defaultapp.utils.NamedParamStatement;
import lombok.Data;
import lombok.SneakyThrows;

import javax.persistence.Entity;
import java.util.List;

@Data
@Entity
public class Bird extends Animal {
    private int flyHeight;

    @Override
    public List<String> getQueryInsertParameters() {
        List<String> parentList = super.getQueryInsertParameters();
        parentList.add("fly_height");
        return parentList;
    }

    @SneakyThrows
    public NamedParamStatement addBatch(NamedParamStatement namedParamStatement) {
        return namedParamStatement.setString("name", this.getName())
                .setInt("zoo_id", this.getZoo().getId())
                .setInt("fly_height", this.getFlyHeight())
                .setString("dtype", this.getClass().getSimpleName())
                .addBatch();
    }
}
