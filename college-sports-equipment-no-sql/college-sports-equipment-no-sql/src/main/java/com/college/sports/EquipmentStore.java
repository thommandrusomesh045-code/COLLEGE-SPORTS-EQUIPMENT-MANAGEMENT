package com.college.sports;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class EquipmentStore {
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    private final Path dataFile = Paths.get("data", "sports-data.json");
    private List<Equipment> equipment = new ArrayList<>();
    private List<IssueRecord> history = new ArrayList<>();
    private long nextEquipmentId=1, nextIssueId=1;

    public EquipmentStore() { load(); }

    private synchronized void load() {
        try {
            if (Files.exists(dataFile)) {
                Map<String,Object> saved=mapper.readValue(dataFile.toFile(), new TypeReference<>() {});
                equipment=mapper.convertValue(saved.get("equipment"), new TypeReference<List<Equipment>>() {});
                history=mapper.convertValue(saved.get("history"), new TypeReference<List<IssueRecord>>() {});
                nextEquipmentId=((Number)saved.get("nextEquipmentId")).longValue();
                nextIssueId=((Number)saved.get("nextIssueId")).longValue();
            }
        } catch(Exception ex) {
            throw new IllegalStateException("Could not read data/sports-data.json", ex);
        }
    }
    private synchronized void save() {
        try {
            Files.createDirectories(dataFile.getParent());
            Map<String,Object> saved=new LinkedHashMap<>();
            saved.put("equipment",equipment); saved.put("history",history);
            saved.put("nextEquipmentId",nextEquipmentId); saved.put("nextIssueId",nextIssueId);
            mapper.writerWithDefaultPrettyPrinter().writeValue(dataFile.toFile(),saved);
        } catch(Exception ex) {
            throw new IllegalStateException("Could not save project data.", ex);
        }
    }
    public synchronized List<Equipment> allEquipment(){ return new ArrayList<>(equipment); }
    public synchronized List<IssueRecord> allHistory(){ return new ArrayList<>(history); }

    public synchronized Equipment addEquipment(String name,String sport,int qty) {
        if(name==null || name.isBlank() || sport==null || sport.isBlank() || qty<=0)
            throw new IllegalArgumentException("Enter a name, sport and quantity greater than zero.");
        Equipment e=new Equipment(nextEquipmentId++,name.trim(),sport.trim(),qty);
        equipment.add(e); save(); return e;
    }
    public synchronized IssueRecord issue(long equipmentId,String student,String roll,int qty) {
        Equipment e=findEquipment(equipmentId);
        if(student==null || student.isBlank() || roll==null || roll.isBlank())
            throw new IllegalArgumentException("Student name and roll number are required.");
        if(qty<=0 || qty>e.availableQuantity)
            throw new IllegalArgumentException("Not enough usable stock. Available: "+e.availableQuantity);
        e.availableQuantity-=qty; e.issuedQuantity+=qty;
        IssueRecord r=new IssueRecord(nextIssueId++,e,student.trim(),roll.trim(),qty);
        history.add(r); save(); return r;
    }
    public synchronized IssueRecord returnItems(long issueId,int qty,int damaged) {
        IssueRecord r=history.stream().filter(x->x.id==issueId).findFirst()
            .orElseThrow(()->new IllegalArgumentException("Issue record not found."));
        if(qty<=0 || qty>r.getPendingQuantity())
            throw new IllegalArgumentException("Return quantity must be within pending quantity.");
        if(damaged<0 || damaged>qty)
            throw new IllegalArgumentException("Damaged quantity must be between zero and returned quantity.");
        Equipment e=findEquipment(r.equipmentId);
        r.returnedQuantity+=qty; r.damagedQuantity+=damaged;
        r.lastReturnDate=LocalDateTime.now().toString();
        e.issuedQuantity-=qty; e.damagedQuantity+=damaged;
        e.availableQuantity+=(qty-damaged);
        save(); return r;
    }
    private Equipment findEquipment(long id) {
        return equipment.stream().filter(e->e.id==id).findFirst()
            .orElseThrow(()->new IllegalArgumentException("Equipment not found."));
    }
    public synchronized List<Map<String,Object>> summary() {
        Map<String,Map<String,Object>> grouped=new LinkedHashMap<>();
        for(Equipment e:equipment) {
            Map<String,Object> row=grouped.computeIfAbsent(e.sport,s->{
                Map<String,Object> m=new LinkedHashMap<>();
                m.put("sport",s);m.put("available",0);m.put("issued",0);m.put("damaged",0);return m;
            });
            row.put("available",(Integer)row.get("available")+e.availableQuantity);
            row.put("issued",(Integer)row.get("issued")+e.issuedQuantity);
            row.put("damaged",(Integer)row.get("damaged")+e.damagedQuantity);
        }
        return new ArrayList<>(grouped.values());
    }
}
