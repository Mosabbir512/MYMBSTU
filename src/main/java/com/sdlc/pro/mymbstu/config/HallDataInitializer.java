package com.sdlc.pro.mymbstu.config;

import com.sdlc.pro.mymbstu.model.*;
import com.sdlc.pro.mymbstu.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class HallDataInitializer {

    @Bean
    public CommandLineRunner initializeHallData(HallRepository hallRepository) {
        return args -> {
            if (hallRepository.count() == 0) {
                // Create floors 1-5
                for (int floor = 1; floor <= 5; floor++) {
                    // Create rooms 01-20 for each floor
                    for (int room = 1; room <= 5; room++) {
                        String roomNumber = String.format("%d%02d", floor, room);

                        // Create seats A-D for each room
                        for (char seat = 'A'; seat <= 'D'; seat++) {
                            hallRepository.save(
                                    new JananetaAbdulMannanHall(
                                            roomNumber,
                                            String.valueOf(seat),
                                            false // status = false (available)
                                    )
                            );
                        }
                    }
                }
                System.out.println("Initial hall data created successfully");
            }
        };
    }
    @Bean
    @Order(2) // Run after other initializers if needed
    public CommandLineRunner initializeBangabandhuHallData(BangabandhuSheikhMujiburRahmanHallRepository hallRepository) {
        return args -> {
            // Check if data already exists
            if (hallRepository.count() == 0) {
                System.out.println("Initializing Bangabandhu Sheikh Mujibur Rahman Hall data...");

                for (int floor = 1; floor <= 5; floor++) {

                    for (int room = 1; room <= 10; room++) {
                        String roomNumber = String.format("%d%02d", floor, room);
                        for (char seat = 'A'; seat <= 'D'; seat++) {
                            BangabandhuSheikhMujiburRahmanHall hallSeat = new BangabandhuSheikhMujiburRahmanHall(
                                    roomNumber,
                                    String.valueOf(seat),
                                    false
                            );
                            hallRepository.save(hallSeat);
                        }
                    }
                }
                System.out.println("Bangabandhu Sheikh Mujibur Rahman Hall data initialized successfully");
                System.out.println("Total seats created: " + (5 * 25 * 4)); // 5 floors × 25 rooms × 4 seats
            }
        };
    }

    @Bean
    @Order(3) // Run after other initializers
    public CommandLineRunner initializeSheikhRaselHallData(SheikhRaselHallRepository hallRepository) {
        return args -> {
            if (hallRepository.count() == 0) {
                System.out.println("Initializing Sheikh Rasel Hall data...");

                List<SheikhRaselHall> seats = new ArrayList<>();

                // 5 floors
                for (int floor = 1; floor <= 5; floor++) {
                    // 25 rooms per floor
                    for (int room = 1; room <= 10; room++) {
                        String roomNumber = String.format("%d%02d", floor, room);

                        // 4 seats per room (A-D)
                        for (char seat = 'A'; seat <= 'D'; seat++) {
                            seats.add(new SheikhRaselHall(
                                    roomNumber,
                                    String.valueOf(seat),
                                    false // status=false means available
                            ));
                        }
                    }
                }

                hallRepository.saveAll(seats);
                System.out.println("Initialized " + seats.size() + " seats in Sheikh Rasel Hall");
            }
        };
    }

    @Bean
    @Order(4) // Run after other hall initializers
    public CommandLineRunner initializeAlemaKhatunBhashaniHallData(
            AlemaKhatunBhashaniHallRepository hallRepository) {
        return args -> {
            if (hallRepository.count() == 0) {
                System.out.println("Initializing Alema Khatun Bhashani Hall data...");

                List<AlemaKhatunBhashaniHall> seats = new ArrayList<>();

                // 5 floors (1-5)
                for (int floor = 1; floor <= 5; floor++) {
                    // 20 rooms per floor (01-20)
                    for (int room = 1; room <= 10; room++) {
                        String roomNumber = String.format("%d%02d", floor, room);

                        // 4 seats per room (A-D)
                        for (char seat = 'A'; seat <= 'D'; seat++) {
                            seats.add(new AlemaKhatunBhashaniHall(
                                    roomNumber,
                                    String.valueOf(seat),
                                    false // status=false means available
                            ));
                        }
                    }
                }

                hallRepository.saveAll(seats);
                System.out.println("Initialized " + seats.size() + " seats in Alema Khatun Bhashani Hall");
                System.out.println("(5 floors × 20 rooms × 4 seats = 400 total seats)");
            }
        };
    }
    @Bean
    @Order(5)
    public CommandLineRunner initializeBangamataHallData(BangamataHallRepository hallRepository) {
        return args -> {
            if (hallRepository.count() == 0) {
                System.out.println("Initializing Bangamata Sheikh Fazilatunnesa Mujib Hall data...");
                List<BangamataSheikhFazilatunnesaMujibHall> seats = new ArrayList<>();

                for (int floor = 1; floor <= 5; floor++) {
                    for (int room = 1; room <= 10; room++) {
                        String roomNumber = String.format("%d%02d", floor, room);

                        for (char seat = 'A'; seat <= 'D'; seat++) {
                            seats.add(new BangamataSheikhFazilatunnesaMujibHall(
                                    roomNumber,
                                    String.valueOf(seat),
                                    false
                            ));
                        }
                    }
                }

                hallRepository.saveAll(seats);
                System.out.println("Initialized " + seats.size() + " seats in Bangamata Hall");
                System.out.println("(5 floors × 20 rooms × 4 seats = 400 total seats)");
            }
        };
    }
    @Bean
    @Order(6)
    public CommandLineRunner initializeShahidZiaurRahmanHallData(
            ShahidZiaurRahmanHallRepository hallRepository) {
        return args -> {
            if (hallRepository.count() == 0) {
                System.out.println("Initializing Shahid Ziaur Rahman Hall data...");

                List<ShahidZiaurRahmanHall> seats = new ArrayList<>();
                int floor = 1; // Only 1 floor

                // 15 rooms (01-15)
                for (int room = 1; room <= 5; room++) {
                    String roomNumber = String.format("%d%02d", floor, room);

                    // 4 seats per room (A-D)
                    for (char seat = 'A'; seat <= 'D'; seat++) {
                        seats.add(new ShahidZiaurRahmanHall(
                                roomNumber,
                                String.valueOf(seat),
                                false // status=false means available
                        ));
                    }
                }

                hallRepository.saveAll(seats);
                System.out.println("Initialized " + seats.size() + " seats in Shahid Ziaur Rahman Hall");
                System.out.println("(1 floor × 15 rooms × 4 seats = 60 total seats)");
            }
        };
    }
    @Bean
    @Order(7)
    public CommandLineRunner initializeShaheedJananiJahanaraImamHallData(
            ShaheedJananiJahanaraImamHallRepository hallRepository) {
        return args -> {
            if (hallRepository.count() == 0) {
                System.out.println("haheedJananiJahanaraImam Hall data...");

                List<ShaheedJananiJahanaraImamHall> seats = new ArrayList<>();
                int floor = 1;
                for (int room = 1; room <= 5; room++) {
                    String roomNumber = String.format("%d%02d", floor, room);
                    
                    for (char seat = 'A'; seat <= 'D'; seat++) {
                        seats.add(new ShaheedJananiJahanaraImamHall(
                                roomNumber,
                                String.valueOf(seat),
                                false
                        ));
                    }
                }

                hallRepository.saveAll(seats);
                System.out.println("Initialized " + seats.size() + " seats in Shaheed Janani Jahanara Imam Hall");
                System.out.println("(1 floor × 5 rooms × 4 seats = 60 total seats)");
            }
        };
    }


}