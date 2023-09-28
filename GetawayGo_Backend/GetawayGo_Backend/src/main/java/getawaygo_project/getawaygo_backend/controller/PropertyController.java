package getawaygo_project.getawaygo_backend.controller;

import getawaygo_project.getawaygo_backend.business.*;
import getawaygo_project.getawaygo_backend.configuration.isauthenticated.IsAuthenticated;
import getawaygo_project.getawaygo_backend.domain.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/properties")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class PropertyController {
    private final CreatePropertyUseCase createPropertyUseCase;
    private final GetAllPropertiesUseCase getAllPropertiesUseCase;
    private final GetPropertyUseCase getPropertyUseCase;
    private final DeletePropertyUseCase deletePropertyUseCase;
    private final UpdatePropertyUseCase updatePropertyUseCase;
    private final GetPropertiesByCountryUseCase getPropertiesByCountryUseCase;
    private final GetPropertiesForHostUseCase getPropertiesForHostUseCase;
    private final UploadPicturesForPropertyUseCase uploadPicturesForPropertyUseCase;
    private final FilterPropertiesUseCase filterPropertiesUseCase;
    private final GetHighestPropertyPrice getHighestPropertyPrice;

    @IsAuthenticated
    @RolesAllowed("ROLE_HOST")
    @PostMapping("{userId}")
    public ResponseEntity<CreatePropertyResponse> createProperty(@PathVariable("userId") long id,
                                                                 @RequestBody @Valid CreatePropertyRequest request) {
        request.setUserId(id);
        CreatePropertyResponse response = createPropertyUseCase.createProperty(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<GetAllPropertiesResponse> getAllProperties() {
        GetAllPropertiesResponse response = getAllPropertiesUseCase.getProperties();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/max")
    public ResponseEntity<Double> getHighestPrice() {
        Double max = getHighestPropertyPrice.getHighestPropertyPrice();
        return ResponseEntity.ok(max);
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<GetAllPropertiesResponse> getAllPropertiesByCountry(@PathVariable(value = "country") final String country) {
        GetAllPropertiesResponse response = getPropertiesByCountryUseCase.getPropertiesByCountry(country);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{town}/{max}")
    public ResponseEntity<GetAllPropertiesResponse> getFilteredProperties(@PathVariable(value = "town") final String town, @PathVariable(value = "max") final double max) {
        GetAllPropertiesResponse response = filterPropertiesUseCase.getFilteredProperties(max, town);
        return ResponseEntity.ok(response);
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_HOST")
    @GetMapping("/host/{id}")
    public ResponseEntity<GetAllPropertiesResponse> getAllPropertiesForHost(@PathVariable(value = "id") final long id) {
        GetAllPropertiesResponse response = getPropertiesForHostUseCase.getPropertiesForHost(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<Property> getProperty(@PathVariable(value = "id") final long id) {
        final Optional<Property> property = getPropertyUseCase.getProperty(id);
        if (property.isPresent())
            return ResponseEntity.ok().body(property.get());
        return ResponseEntity.notFound().build();
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_HOST", "ROLE_ADMIN"})
    @DeleteMapping("{propertyId}")
    public ResponseEntity<Void> deleteProperty(@PathVariable long propertyId) {
        deletePropertyUseCase.deleteProperty(propertyId);
        return ResponseEntity.noContent().build();
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_HOST")
    @PutMapping()
    public ResponseEntity<Void> updateProperty(@RequestBody @Valid UpdatePropertyRequest request) {
        updatePropertyUseCase.updateProperty(request);
        return ResponseEntity.noContent().build();
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_HOST"})
    @PutMapping("/photos/{id}")
    public ResponseEntity<Void> uploadPhotos(@RequestParam("photos") List<MultipartFile> photos, @PathVariable(value = "id") final long id) {
        uploadPicturesForPropertyUseCase.uploadPictures(photos, id);
        return ResponseEntity.noContent().build();
    }
}
