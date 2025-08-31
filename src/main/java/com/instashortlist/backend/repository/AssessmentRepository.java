package com.instashortlist.backend.repository;

import com.instashortlist.backend.model.Assessment;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AssessmentRepository extends ReactiveCrudRepository<Assessment, Long> {

    // ✅ Explicit join query to fetch by job ID
    @Query("""
           SELECT a.* 
           FROM isl_assessments a
           JOIN isl_job_assessments ja ON a.id = ja.assessment_id
           WHERE ja.job_id = :jobId
           ORDER BY a.step_order ASC
           """)
    Flux<Assessment> findByJobIdOrderByStepOrderAsc(@Param("jobId") Long jobId);

    // ✅ Alias method
    @Query("""
           SELECT a.* 
           FROM isl_assessments a
           JOIN isl_job_assessments ja ON a.id = ja.assessment_id
           WHERE ja.job_id = :jobId
           ORDER BY a.step_order ASC
           """)
    Flux<Assessment> findAssessmentsByJobId(@Param("jobId") Long jobId);

    // ✅ Find all steps for a given assessment title
    @Query("""
           SELECT * 
           FROM isl_assessments 
           WHERE title = :title
           ORDER BY step_order ASC
           """)
    Flux<Assessment> findByTitleOrderByStepOrderAsc(@Param("title") String title);

    // ✅ Insert link between job and assessment step
    @Query("""
           INSERT INTO isl_job_assessments (job_id, assessment_id)
           VALUES (:jobId, :assessmentId)
           ON DUPLICATE KEY UPDATE job_id = job_id
           """)
    Mono<Integer> linkJobToAssessment(@Param("jobId") Long jobId, @Param("assessmentId") Long assessmentId);
}
