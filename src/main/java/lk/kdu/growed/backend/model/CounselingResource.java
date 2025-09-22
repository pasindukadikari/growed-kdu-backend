// CounselingResource.java
package lk.kdu.growed.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "counseling_resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounselingResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceCategory category;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "icon_color", length = 7)
    private String iconColor;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Commented out temporarily to fix the jakarta.annotation.Resource conflict
    // Uncomment and fix when you create your actual Resource entity
    // @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // @OrderBy("displayOrder ASC")
    // private List<ResourceItem> items;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum ResourceCategory {
        CORE_MINDSET, DURING_SESSIONS, PRACTICAL_ETHICAL
    }
}