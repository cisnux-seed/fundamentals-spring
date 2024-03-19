package dev.cisnux.webmvc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSocialMediaRequest {
        private String name;
        private String location;
}
