package com.laterna.xaxathonprime.region;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laterna.xaxathonprime.role.Role;
import com.laterna.xaxathonprime.role.RoleRepository;
import com.laterna.xaxathonprime.user.User;
import com.laterna.xaxathonprime.user.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionDataImportService {
    private final RegionRepository regionRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class RegionData {
        private String subject;
        private Leader leader;
        private String contact;
        private String image_url;
        private String federal_district;

        @Data
        private static class Leader {
            private String family_name;
            private String first_name;
            private String middle_name;
        }
    }

    @Transactional
    public void importData() {
        try {
            var inputStream = getClass().getResourceAsStream("/regions_data.json");
            if (inputStream == null) {
                throw new RuntimeException("Could not find regions_data.json in resources");
            }
            RegionData[] regionsData = objectMapper.readValue(inputStream, RegionData[].class);

            Role leaderRole = roleRepository.findById(2L)
                    .orElseThrow(() -> new RuntimeException("Role with id 2 not found"));

            for (RegionData regionData : regionsData) {
                User user = null;

                if (regionData.getLeader().getFamily_name() != null && regionData.getLeader().getFirst_name() != null) {
                    String email = generateEmail(regionData.getLeader().getFirst_name(), regionData.getLeader().getFamily_name());

                    user = User.builder()
                            .firstname(regionData.getLeader().getFirst_name())
                            .lastname(regionData.getLeader().getFamily_name())
                            .patronymic(regionData.getLeader().getMiddle_name() != null ?
                                    regionData.getLeader().getMiddle_name() : "Отчество")
                            .email(email)
                            .password(passwordEncoder.encode("password"))
                            .role(leaderRole)
                            .emailVerified(true)
                            .build();

                    log.info("Creating user: {} {} with email: {}",
                            user.getFirstname(),
                            user.getLastname(),
                            user.getEmail());
                    userService.save(user);
                }

                // Создаем регион
                Region region = Region.builder()
                        .name(regionData.getSubject())
                        .contactEmail(regionData.getContact())
                        .imageUrl(regionData.getImage_url())
                        .federalDistrict(regionData.getFederal_district()) // Added federal district
                        .user(user)
                        .build();

                regionRepository.save(region);
            }

            log.info("Data import completed successfully. Imported {} regions", regionsData.length);
            for (RegionData data : regionsData) {
                log.debug("Imported region: {} (Federal District: {})",
                        data.getSubject(),
                        data.getFederal_district() != null ? data.getFederal_district() : "Not specified");
            }
        } catch (IOException e) {
            log.error("Error importing data", e);
            throw new RuntimeException("Failed to import data", e);
        }
    }

    private final Map<String, Integer> emailCounters = new HashMap<>();

    private String generateEmail(String firstName, String lastName) {
        String baseEmail = transliterate(firstName.toLowerCase()) + "." +
                transliterate(lastName.toLowerCase());

        String key = baseEmail;
        emailCounters.putIfAbsent(key, 0);
        int counter = emailCounters.get(key);
        emailCounters.put(key, counter + 1);

        return baseEmail + (counter == 0 ? "" : counter) + "@xaxathon.ru";
    }

    private String transliterate(String text) {
        return text.toLowerCase()
                .replace("а", "a").replace("б", "b")
                .replace("в", "v").replace("г", "g")
                .replace("д", "d").replace("е", "e")
                .replace("ё", "e").replace("ж", "zh")
                .replace("з", "z").replace("и", "i")
                .replace("й", "y").replace("к", "k")
                .replace("л", "l").replace("м", "m")
                .replace("н", "n").replace("о", "o")
                .replace("п", "p").replace("р", "r")
                .replace("с", "s").replace("т", "t")
                .replace("у", "u").replace("ф", "f")
                .replace("х", "h").replace("ц", "ts")
                .replace("ч", "ch").replace("ш", "sh")
                .replace("щ", "sch").replace("ъ", "")
                .replace("ы", "y").replace("ь", "")
                .replace("э", "e").replace("ю", "yu")
                .replace("я", "ya");
    }
}