package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Logging {
    public Integer id;
    public String description;
    public String ip;
    public String endpoint;
    public Timestamp timestamp;
}
