package rs.sons.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int invoice_id;
	
	@NotNull
	private Integer invoice_preinvoice_month = 0;
	
	@NotNull
	private Integer invoice_preinvoice_year = 0;
	
	@NotNull
	private Integer invoice_preinvoice_number = 0;
	
	@NotNull
	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer invoice_month = 0;
	
	@NotNull
	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer invoice_year = 0;
	
	@NotNull
	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer invoice_number = 0;
	
	@NotNull
	@Column(length = Integer.MAX_VALUE)
	@Lob
	private String invoice_client_data;
	
	@NotNull
	@Column(columnDefinition="DATETIME DEFAULT CURRENT_TIMESTAMP")
	private Date invoice_creation_date;
	
	@NotNull
	@Column(columnDefinition="DATETIME DEFAULT '0000-00-00 00:00:00'")
	private Date invoice_payment_deadline;
	
	@NotNull
	@Column(columnDefinition="DATETIME DEFAULT '0000-00-00 00:00:00'")
	private Date invoice_payment_date_real;
	
	@NotNull
	@Column(columnDefinition="DATETIME DEFAULT '0000-00-00 00:00:00'")
	private Date invoice_payment_date_for_print;
	
	@NotNull
	@Column(nullable = false, columnDefinition = "TINYINT NOT NULL DEFAULT '1'", length = 1)
	private boolean invoice_is_preinvoice = true;
	
	@NotNull
	@Column(nullable = false, columnDefinition = "TINYINT NOT NULL DEFAULT '0'", length = 1)
	private boolean invoice_is_invoice = false;
	
	//datum prometa
	@NotNull
	@Column(columnDefinition="DATETIME DEFAULT '0000-00-00 00:00:00'")
	private Date invoice_delivery_date;
	
	@NotNull
	@Column(nullable = false, columnDefinition = "TINYINT NOT NULL DEFAULT '0'", length = 1)
	private boolean invoice_payed;
	
	@NotNull
	@ManyToOne()
	@JoinColumn(name = "invoice_client_id", columnDefinition = "int default 0")
	private Client client;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "invoice_created_by", columnDefinition = "int default 0")
	private User user;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "invoice_payment_applied_by", columnDefinition = "int default 0")
	private User userApplied;
	
	@OneToMany(mappedBy = "invoice")
	private List<InvoiceItem> invoiceItems;
	
}
