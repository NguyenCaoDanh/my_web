package com.danhcaonguyen.web.controller;

import com.danhcaonguyen.web.dto.RequestResponse;
import com.danhcaonguyen.web.dto.response.ActivityResponse;
import com.danhcaonguyen.web.entity.Activities;
import com.danhcaonguyen.web.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller quản lý các endpoint REST cho hoạt động (Activities).
 * This controller handles CRUD operations for the "Activities" entity
 * and provides endpoints for creating, updating, retrieving, and deleting activities.
 */
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    // Dependency injection for ActivityService
    @Autowired
    private ActivityService activityService;

    /**
     * Endpoint to create a new activity.
     * @param activities The activity to be created, received from the request body.
     * @return ResponseEntity containing a success message.
     */
    @PostMapping
    public ResponseEntity<RequestResponse> createActivity(@RequestBody Activities activities) {
        activityService.save(activities);
        return ResponseEntity.ok(new RequestResponse("Activity created successfully"));
    }

    /**
     * Endpoint to update an existing activity.
     * @param id The ID of the activity to be updated.
     * @param updatedActivity The updated activity details, received from the request body.
     * @return ResponseEntity containing the updated activity.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RequestResponse> updateActivity(@PathVariable Integer id, @RequestBody Activities updatedActivity) {
        // Find the activity by its ID
        Activities activity = activityService.findOne(id);
        if (activity == null) {
            return ResponseEntity.notFound().build(); // Handle case where activity is not found
        }

        // Update activity details
        activity.setTitle(updatedActivity.getTitle());
        activity.setDescription(updatedActivity.getDescription());

        // Save the updated activity
        activityService.save(activity);
        return ResponseEntity.ok(new RequestResponse(activity));
    }

    /**
     * Endpoint to retrieve details of a specific activity by ID.
     * @param id The ID of the activity to retrieve.
     * @return ResponseEntity containing the activity details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> getActivity(@PathVariable Integer id) {
        ActivityResponse activityResponse = activityService.findById(id);
        return ResponseEntity.ok(activityResponse);
    }


    /**
     * Endpoint to delete an activity by ID.
     * @param id The ID of the activity to be deleted.
     * @return ResponseEntity containing a success message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RequestResponse> deleteActivity(@PathVariable Integer id) {
        activityService.delete(id); // Xóa hoạt động
        return ResponseEntity.ok(new RequestResponse("Activity deleted successfully"));
    }

    /**
     * Endpoint to retrieve all activities with pagination support.
     * @param pageable Pagination details (page number, size, etc.) provided by Spring Data.
     * @return ResponseEntity containing a paginated list of activities.
     */
    @GetMapping
    public ResponseEntity<Page<ActivityResponse>> getAllActivities(Pageable pageable) {
        // Fetch all activities with pagination
        Page<Activities> activitiesPage = activityService.findAll(pageable);

        // Map each Activities entity to an ActivityResponse DTO
        Page<ActivityResponse> responsePage = activitiesPage.map(activity -> {
            ActivityResponse response = new ActivityResponse();
            response.setTitle(activity.getTitle());
            response.setDescription(activity.getDescription());

            // Add the path for the getById endpoint
            response.setPath("/api/activities/" + activity.getIdActivities());
            return response;
        });

        // Return the paginated response
        return ResponseEntity.ok(responsePage);
    }


}
