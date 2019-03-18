package rs.sons.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//https://web.archive.org/web/20100201003834/http://notesonjava.wordpress.com/2008/11/03/managing-the-bidirectional-relationship/

@Entity
@Getter
@Setter
@NoArgsConstructor
public class InvoiceItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int invoice_item_id;
	
	@Column(nullable = false, columnDefinition = "Decimal(14,2) default '0.00'")
	private double invoice_item_price;
	
	@Column(nullable = false, columnDefinition = "int default 0")
	private int invoice_item_vat;
	
	@Column(nullable = false, columnDefinition = "Decimal(14,2) default '0.00'")
	private double invoice_item_discount;
	
	@NotNull
	@Column(length = Integer.MAX_VALUE)
	@Lob
	private String invoice_item_description;
	
	
	private String invoice_item_unit;
	
	@Column(nullable = false, columnDefinition = "Decimal(14,2)")
	private double invoice_item_amount;
	
	@ManyToOne()
	@JoinColumn(name = "invoice_item_invoice_id")
	private Invoice invoice;
}
