package com.college.sports;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class EquipmentController {
    private final EquipmentStore store;
    public EquipmentController(EquipmentStore store){this.store=store;}
    @GetMapping("/equipment") public List<Equipment> equipment(){return store.allEquipment();}
    @GetMapping("/history") public List<IssueRecord> history(){return store.allHistory();}
    @GetMapping("/summary") public List<Map<String,Object>> summary(){return store.summary();}
    @PostMapping("/equipment") public Equipment add(@RequestBody AddRequest r){return store.addEquipment(r.name,r.sport,r.quantity);}
    @PostMapping("/issue") public IssueRecord issue(@RequestBody IssueRequest r){return store.issue(r.equipmentId,r.studentName,r.studentRollNo,r.quantity);}
    @PostMapping("/return/{id}") public IssueRecord returnItems(@PathVariable long id,@RequestBody ReturnRequest r){return store.returnItems(id,r.quantity,r.damagedQuantity);}
    public static class AddRequest {public String name,sport;public int quantity;}
    public static class IssueRequest {public long equipmentId;public String studentName,studentRollNo;public int quantity;}
    public static class ReturnRequest {public int quantity,damagedQuantity;}
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    public Map<String,String> badRequest(IllegalArgumentException e){return Map.of("message",e.getMessage());}
}