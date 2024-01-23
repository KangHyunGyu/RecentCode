const divisions = [
  "Sales Manager (FS/SCR) - Project Manager (ADM)",
  "Project Manager",
  "Product  Engineer (PAE / SAE/  ADM-DE)",
  "Industrial Engineer / Plant Representative",
  "Quality Engineer",
  "Project Buyer",
  "Controlled System Application Engineer"
];

const selectOptions = [
  { title: "", classname: "none" },
  { title: "G", classname: "signal_G" },
  { title: "Y", classname: "signal_Y" },
  { title: "R", classname: "signal_R" },
  { title: "N/A", classname: "signal_NA" },
];


const rows = {
  	division1: [
  		{
  			number: "SM-01<br/>ADM PM-01",
  			deliverables: "All customer specifications & contractual obligations available and reviewed"
  		},
  		{
  			number: "SM-02<br/>ADM PM-02",
  			deliverables: "Global deviations and exceptions list"
  		},
  		{
  			number: "SM-03",
  			deliverables: "Responsibility matrix for directed suppliers"
  		},
  		{
  			number: "SM-04",
  			deliverables: "Team offer approval"
  		},
  		{
  			number: "SM-05",
  			deliverables: "Internal validation of offer and degree of freedom for negotiation"
  		},
  		{
  			number: "SM-06<br/>ADM PM-03",
  			deliverables: "Offer to Customer"
  		},
  		{
  			number: "SM-07",
  			deliverables: "Customer contract review"
  		},
  		{
  			number: "SM-08",
  			deliverables: "Information shared in Acknowledgement of Order"
  		},
  		{
  			number: "SM-09",
  			deliverables: "Customer order"
  		},
  		{
  			number: "SM-10",
  			deliverables: "Customer request for change"
  		},
  		{
  			number: "SM-11",
  			deliverables: "Quote of change"
  		},
  		{
  			number: "SM-12",
  			deliverables: "Invoice"
  		},
  		{
  			number: "SM-13",
  			deliverables: "Payment"
  		},
  		{
  			number: "SM-14",
  			deliverables: "Customer authorization for tooling kickoff"
  		},
  		{
  			number: "SM-15",
  			deliverables: "Ramp-up and production volume forecasts"
  		},
  		{
  			number: "SM-16",
  			deliverables: "List of customer owned tooling"
  		},
  		{
  			number: "SM-17",
  			deliverables: "Service parts contract"
  		},
  		{
  			number: "SM-18",
  			deliverables: "yLoss of businness analysis (Ph1)<br/>Customer satisfaction survey (Ph6)"
  		}
  	],
  	division2: [
  		{
  			number: "PM-01",
  			deliverables: "Development cost budget built with functions and countries"
  		},
  		{
  			number: "PM-02<br/>ADM PM-04",
  			deliverables: "Project timing plan<br/>ADM Project Timing"
  		},
  		{
  			number: "PM-03<br/>ADM PM-05",
  			deliverables: "Regular profitability study (stopINvest)<br/>ADM StopINvest"
  		},
  		{
  			number: "PM-04<br/>ADM PM-06",
  			deliverables: "Regular QCTR Assessment<br/>ADM Monthly Report"
  		},
  		{
  			number: "PM-05<br/>ADM PM-07",
  			deliverables: "Development kickoff<br/>ADM Project Kick-off"
  		},
  		{
  			number: "PM-06<br/>ADM PM-08",
  			deliverables: "Part delivery quantity plan<br/>ADM Part Delivery Plan"
  		},
  		{
  			number: "PM-07<br/>ADM PM-09",
  			deliverables: "Team feasibility commitment<br/>ADM Team Feasibility Commitment"
  		},
  		{
  			number: "PM-08<br/>ADM PM-10",
  			deliverables: "Lessons learned from project dev.<br/>ADM Lessons Learned"
  		},
  		{
  			number: "PM-09<br/>ADM PM-11",
  			deliverables: "Handover meeting with plant<br/>ADM Plant Handover"
  		}
  	],
  	division3: [
  		{
  			number: "PAE-01<br/>SAE-01<br/>ADM DE-01",
  			deliverables: "Deviations and exceptions list<br/>System Deviations and exceptions list<br/>ADM Deviation list"
  		},
  		{
  			number: "PAE-02<br/>SAE-02<br/>ADM DE-02",
  			deliverables: "Product design proposal, feasibility<br/>Syst. Architecture & Design Proposal, feasibility<br/>ADM Technical Proposal" 
  		},
  		{
  			number: "PAE-03<br/>SAE-03<br/>ADM DE-09",
  			deliverables: "Validation plan and timing <br/>(approved by customer)<br/>Syst. Validation plan (incl. EMC) and timing (approved by customer)<br/>ADM DV & PV Plan"
  		},
  		{
  			number: "PAE-04<br/>SAE-04<br/>ADM DE-04",
  			deliverables: "BOM (breakdown, COP, serviceability, service parts and make or buy analysis)<br/>BOM (COP, serviceability, service parts, Base lines and make or buy)<br/>ADM BOM" 
  		},
  		{
  			number: "PAE-05<br/>SAE-05<br/>ADM DE-10",
  			deliverables: "Product feasibility review / Design review / Design freeze<br/>System Feasibility Review / Design Review / Design Freeze<br/>ADM Design Review" 
  		},
  		{
  			number: "PAE-06<br/>SAE-06<br/>ADM DE-08",
  			deliverables: "Simulation / calculation reports<br/>Stack-up analysis<br/>Simulation and calculation reports<br/>ADM Simulation and calculation reports" 
  		},
  		{
  			number: "PAE-07<br/>SAE-07<br/>ADM DE-05",
  			deliverables: "DFMEA (incl Functional Analysis)<br/>System FMEA<br/>ADM DFMEA" 
  		},
  		{
  			number: "PAE-08<br/>ADM DE-15",
  			deliverables: "Key product characteristics <br/>(list if not specified on drawings)<br/>ADM KPC list" 
  		},
  		{
  			number: "PAE-09<br/>SAE-08<br/>ADM DE-14",
  			deliverables: "Drawing release (approved by customer)<br/>System release (drawings, HW & SW) approved by customer<br/>ADM Assembly Drawing" 
  		},
  		{
  			number: "PAE-10<br/>SAE-09<br/>ADM DE-06",
  			deliverables: "Tech. specifications of components<br/>Component Tech.specification(HW/SW)<br/>ADM Components Tech. Specification" 
  		},
  		{
  			number: "PAE-11<br/>SAE-10<br/>ADM DE-13",
  			deliverables: "Component drawings approved<br/>Component drawings approved<br/>ADM Components Drawings" 
  		},
  		{
  			number: "PAE-12<br/>SAE-11<br/>ADM DE-07",
  			deliverables: "Component validation plans approved<br/>Component validation plans (incl. EMC) and timing (SW/HW) approved<br/>ADM Components Validation Plans" 
  		},
  		{
  			number: "PAE-13<br/>SAE-12<br/>ADM DE-11",
  			deliverables: "Component validation reports approved<br/>Component validation reports approved<br/>ADM Components Validation Reports" 
  		},
  		{
  			number: "PAE-14<br/>SAE-13<br/>ADM DE-03",
  			deliverables: "Product sub timing (design activities)<br/>Product sub-timing<br/>ADM Product Sub-timing"
  		},
  		{
  			number: "PAE-15",
  			deliverables: "Good quality parts delivered on time to customer" 
  		},
  		{
  			number: "PAE-16<br/>SAE-14<br/>ADM DE-12",
  			deliverables: "Validation reports (approved by customer)<br/>System Validation reports<br/>(approved by customer)<br/>ADM DV & PV Report" 
  		},
  		{
  			number: "PAE-17<br/>SAE-15",
  			deliverables: "Homologation certificate"
  		},
  		{
  			number: "ADM DE-16",
  			deliverables: "ADM PDS"
  		}
  	],
  	division4: [
  		{
  			number: "IE-01",
  			deliverables: "Industrial scenario with process proposal" 
  		},
  		{
  			number: "IE-02",
  			deliverables: "Deviations and exceptions list" 
  		},
  		{
  			number: "IE-03",
  			deliverables: "Matrix for shared responsibility between industrial and plant (RASIC)" 
  		},
  		{
  			number: "IE-04",
  			deliverables: "Capacitary availability plan"
  		},
  		{
  			number: "IE-05",
  			deliverables: "CAPEX plan (capacitary and specific tooling list)" 
  		},
  		{
  			number: "IE-06",
  			deliverables: "Industrial timing plan (incl. process activities and production tooling)" 
  		},
  		{
  			number: "IE-07",
  			deliverables: "DFM&A" 
  		},
  		{
  			number: "IE-08",
  			deliverables: "Blow Molding / Injection Trials preparation and synthesis" 
  		},
  		{
  			number: "IE-09",
  			deliverables: "Process flow chart" 
  		},
  		{
  			number: "IE-10",
  			deliverables: "PFMEA" 
  		},
  		{
  			number: "IE-11",
  			deliverables: "Key control characteristics" 
  		},
  		{
  			number: "IE-12",
  			deliverables: "Error-proofing" 
  		},
  		{
  			number: "IE-13",
  			deliverables: "POMS event and checklist" 
  		},
  		{
  			number: "IE-14",
  			deliverables: "Packaging definition and validation for finished goods, WIP and service parts" 
  		},
  		{
  			number: "IE-15",
  			deliverables: "Validation of component packaging and logistic (FS)<br/>Validation of component packaging and logistic (SCR-ADM)"
  		},
  		{
  			number: "IE-16",
  			deliverables: "Capacitary Machines" 
  		},
  		{
  			number: "IE-17",
  			deliverables: "Blow Molding / Injection specific tooling" 
  		},
  		{
  			number: "IE-18",
  			deliverables: "Specific Machines" 
  		},
  		{
  			number: "IE-19",
  			deliverables: "Ergonomy & Plant Safety check" 
  		},
  		{
  			number: "IE-20",
  			deliverables: "Working Instruction and Training of production staff" 
  		},
  		{
  			number: "IE-21",
  			deliverables: "Run at rate (internal & external)" 
  		},
  		{
  			number: "IE-22",
  			deliverables: "Ramp-up cost follow-up and plant readiness" 
  		}
  	],
  	division5: [
  		{
  			number: "QE-01",
  			deliverables: "Quality proposal for test and measuring devices and quality targets" 
  		},
  		{
  			number: "QE-02",
  			deliverables: "QE Deviations and exceptions list" 
  		},
  		{
  			number: "QE-03",
  			deliverables: "Product and process unchangeable compliance review" 
  		},
  		{
  			number: "QE-04",
  			deliverables: "Quality report and release for laboratory deliveries (incl. BM trial quality report)" 
  		},
  		{
  			number: "QE-05",
  			deliverables: "Quality report and release for customer deliveries" 
  		},
  		{
  			number: "QE-06",
  			deliverables: "Quality dashboard" 
  		},
  		{
  			number: "QE-07",
  			deliverables: "Control plan" 
  		},
  		{
  			number: "QE-08",
  			deliverables: "Recommendations to customer for correct product use" 
  		},
  		{
  			number: "QE-09",
  			deliverables: "IMDS status (or equivalent)" 
  		},
  		{
  			number: "QE-10",
  			deliverables: "Calibration and capabilities validation report of test and measuring devices" 
  		},
  		{
  			number: "QE-11",
  			deliverables: "Capability assessment" 
  		},
  		{
  			number: "QE-12",
  			deliverables: "PPAP approved" 
  		},
  		{
  			number: "QE-13",
  			deliverables: "Process validation audit" 
  		},
  		{
  			number: "QE-14",
  			deliverables: "Non conformance - corrective action tracking (internal and external)" 
  		}
	],
	division6: [
  		{
  			number: "PB-01",
  			deliverables: "PB Deviations and exceptions list" 
  		},
  		{
  			number: "PB-02",
  			deliverables: "Costed BOM (incl. component tooling and specific CAPEX / validation cost)" 
  		},
  		{
  			number: "PB-03",
  			deliverables: "Tooling capacity check" 
  		},
  		{
  			number: "PB-04",
  			deliverables: "Good quality components and available on time"
  		},
  		{
  			number: "PB-05",
  			deliverables: "Supplier timing plans of components and equipment" 
  		},
  		{
  			number: "PB-06",
  			deliverables: "Component validation plans available" 
  		},
  		{
  			number: "PB-07",
  			deliverables: "Component validation reports available" 
  		},
  		{
  			number: "PB-08",
  			deliverables: "Supplier contracts (incl. service parts)" 
  		},
  		{
  			number: "PB-09",
  			deliverables: "Quality requirements and targets accepted by all suppliers" 
  		},
  		{
  			number: "PB-10",
  			deliverables: "Orders for raw material and components (open orders)" 
  		},
  		{
  			number: "PB-11",
  			deliverables: "Orders for all tooling (equipment. components. test. measuring devices)" 
  		},
  		{
  			number: "PB-12",
  			deliverables: "All tooling available on time" 
  		},
  		{
  			number: "PB-13",
  			deliverables: "Component PFMEA approved" 
  		},
  		{
  			number: "PB-14",
  			deliverables: "All components PPAP approved and run-at-rate approved" 
  		}
  	],
  	division7: [
  		{
  			number: "CSAE-01",
  			deliverables: "CS Deviation list" 
  		},
  		{
  			number: "CSAE-02",
  			deliverables: "CS Technical Proposal" 
  		},
  		{
  			number: "CSAE-03",
  			deliverables: "CS Product Sub-timing" 
  		},
  		{
  			number: "CSAE-04",
  			deliverables: "CS BOM" 
  		},
  		{
  			number: "CSAE-05",
  			deliverables: "CS DFMEA & FTA" 
  		},
  		{
  			number: "CSAE-06",
  			deliverables: "CS Components Technical Specification" 
  		},
  		{
  			number: "CSAE-07",
  			deliverables: "CS components DFMEA" 
  		},
  		{
  			number: "CSAE-08",
  			deliverables: "CS Components Validation Plan" 
  		},
  		{
  			number: "CSAE-09",
  			deliverables: "CS Simulation and calculation reports" 
  		},
  		{
  			number: "CSAE-10",
  			deliverables: "CS Validation Plan" 
  		},
  		{
  			number: "CSAE-11",
  			deliverables: "Calibration dataset and tuning test plans" 
  		},
  		{
  			number: "CSAE-12",
  			deliverables: "CS components Design review" 
  		},
  		{
  			number: "CSAE-13",
  			deliverables: "CS Design review" 
  		},
  		{
  			number: "CSAE-14",
  			deliverables: "CS Components Validation Report" 
  		},
  		{
  			number: "CSAE-15",
  			deliverables: "Software delivery note" 
  		},
  		{
  			number: "CSAE-16",
  			deliverables: "CS components drawings" 
  		},
  		{
  			number: "CSAE-17",
  			deliverables: "Calibration dataset and tuning test reports" 
  		},
  		{
  			number: "CSAE-18",
  			deliverables: "CS Validation Reports" 
  		},
  		{
  			number: "CSAE-19",
  			deliverables: "CS components PDS" 
  		},
  		{
  			number: "CSAE-20",
  			deliverables: "CS PDS" 
  		}
  	]
};