package rs.sons.entity;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FormElementsListContainer {

	private List<FormElements> formElement = new LinkedList<FormElements>();
}
