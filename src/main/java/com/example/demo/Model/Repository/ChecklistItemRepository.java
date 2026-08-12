package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.Checklist;
import com.example.demo.Model.Entities.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem,Long> {
    List<ChecklistItem> findByChecklistId(Long checklistId);
    List<ChecklistItem> findByChecklistIdAndCompletado(Long checklistId, boolean completado);
}
