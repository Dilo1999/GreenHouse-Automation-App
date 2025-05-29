package com.example.gh_app;

public class ApiResponse {
    private String numberOfVanillaPlants;
    private String variationOfVanillaPlant;
    private boolean automationFactors;
    private boolean greenhouseMaterials;

    // Getters and Setters
    public String getNumberOfVanillaPlants() {
        return numberOfVanillaPlants;
    }

    public void setNumberOfVanillaPlants(String numberOfVanillaPlants) {
        this.numberOfVanillaPlants = numberOfVanillaPlants;
    }

    public String getVariationOfVanillaPlant() {
        return variationOfVanillaPlant;
    }

    public void setVariationOfVanillaPlant(String variationOfVanillaPlant) {
        this.variationOfVanillaPlant = variationOfVanillaPlant;
    }

    public boolean isAutomationFactors() {
        return automationFactors;
    }

    public void setAutomationFactors(boolean automationFactors) {
        this.automationFactors = automationFactors;
    }

    public boolean isGreenhouseMaterials() {
        return greenhouseMaterials;
    }

    public void setGreenhouseMaterials(boolean greenhouseMaterials) {
        this.greenhouseMaterials = greenhouseMaterials;
    }
}
