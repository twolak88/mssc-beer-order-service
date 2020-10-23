package twolak.springframework.brewery.model.events;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author twolak
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateBeerOrderResult implements Serializable {

    private static final long serialVersionUID = 6068869871374937595L;
    
    private UUID orderId;
    private Boolean isValid;
}
