package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sanapyeong.global.database.utils.EntityTimestamp;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="TBL_MY_UNIVERSE_TRAIN")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MyUniverseTrain extends EntityTimestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String uniqueCode;

    @Column(columnDefinition = "json")
    private String planetStatus;

    @Column(columnDefinition = "json")
    private String planetOrder;

    @Transient
    private ObjectMapper objectMapper = new ObjectMapper();

    public void setPlanetStatus(List<Boolean> planetStatus){
        this.planetStatus = convertBooleanListToJson(planetStatus);
    }

    public List<Boolean> getPlanetStatusAsList(){
        try{
            return objectMapper.readValue(planetStatus, new TypeReference<List<Boolean>>(){});
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void setPlanetOrder(List<Integer> planetOrder){
        this.planetOrder = convertIntegerListToJson(planetOrder);
    }

    public List<Integer> getPlanetOrderAsList(){
        try{
            return objectMapper.readValue(planetOrder, new TypeReference<List<Integer>>(){});
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String convertBooleanListToJson(List<Boolean> list){
        try{
            return objectMapper.writeValueAsString(list);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String convertIntegerListToJson(List<Integer> list){
        try{
            return objectMapper.writeValueAsString(list);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
