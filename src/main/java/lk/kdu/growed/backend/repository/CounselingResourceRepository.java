package lk.kdu.growed.backend.repository;

import lk.kdu.growed.backend.model.CounselingResource;
import lk.kdu.growed.backend.model.CounselingResource.ResourceCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CounselingResourceRepository extends JpaRepository<CounselingResource, Long> {
    List<CounselingResource> findByIsActiveTrueOrderByDisplayOrderAsc();

    List<CounselingResource> findByCategoryAndIsActiveTrueOrderByDisplayOrderAsc(ResourceCategory category);
}
