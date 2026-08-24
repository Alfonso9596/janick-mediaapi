package com.janickmediadb.janickmediaapi.model.response;

import com.janickmediadb.janickmediaapi.model.SeriesModel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeriesResponse extends AbstractResponse {
    private List<SeriesModel> content;
}
