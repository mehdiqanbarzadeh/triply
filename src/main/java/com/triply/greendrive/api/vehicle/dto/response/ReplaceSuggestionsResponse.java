package com.triply.greendrive.api.vehicle.dto.response;

import com.triply.greendrive.config.infrastructure.ResponseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReplaceSuggestionsResponse extends ResponseService {

    List<ReplaceableSuggestionVehicle> replaceableSuggestionVehicles;
}
