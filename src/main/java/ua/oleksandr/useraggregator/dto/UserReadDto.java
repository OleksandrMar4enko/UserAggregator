package ua.oleksandr.useraggregator.dto;

public record UserReadDto(Long id,
                          String username,
                          String name,
                          String surname) {
}
