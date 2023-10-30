package se.lionsinvests.recipes.renderer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RecipeLinkDTO implements Comparable<RecipeLinkDTO> {

        private final String url;
        private final String name;
        private final String image;


        @Override
        public int compareTo(RecipeLinkDTO o) {
            return this.name.compareTo(o.name);
        }
}
