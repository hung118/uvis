package gov.utah.va.vts.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import gov.utah.va.vts.model.BenefitRecipient;
import gov.utah.va.vts.model.BenefitType;
import gov.utah.va.vts.model.CombatZone;
import gov.utah.va.vts.model.Dashboard;
import gov.utah.va.vts.model.DataImport;
import gov.utah.va.vts.model.DecorationMedal;
import gov.utah.va.vts.model.DischargeType;
import gov.utah.va.vts.model.DocType;
import gov.utah.va.vts.model.Ethnicity;
import gov.utah.va.vts.model.NameValue;
import gov.utah.va.vts.model.Note;
import gov.utah.va.vts.model.RecordType;
import gov.utah.va.vts.model.Registration;
import gov.utah.va.vts.model.Relation;
import gov.utah.va.vts.model.Report;
import gov.utah.va.vts.model.ReportProperty;
import gov.utah.va.vts.model.Role;
import gov.utah.va.vts.model.ServiceBranch;
import gov.utah.va.vts.model.ServiceEra;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;
import gov.utah.va.vts.model.VeteranAttachment;
import gov.utah.va.vts.model.VeteranBenefit;
import gov.utah.va.vts.model.VeteranNote;
import gov.utah.va.vts.model.VeteranServicePeriod;

/**
 * interface for the VTS Service
 * 
 * @author hnguyen
 *
 */
public interface VtsService {

	/**
	 * flushes any cached data to the database
	 */
	public void flush();
	
	/**
	 * Clear the persistence context, causing all managed entities to become detached. Changes made to entities that 
	 * have not been flushed to the database will not be persisted.
	 */
	public void clear();
	
	/**
	 * Saves user.
	 * @param user
	 * @return
	 */
	public User saveUser(User user);
	
	/**
	 * Deletes user.
	 * @param user
	 */
	public void deleteUser(User user);
	
	/**
	 * Finds an user by email address.
	 * @param email
	 * @return
	 */
	public User findUserByEmail(String email);
	
	/**
	 * Finds active user by email address.
	 * @param email
	 * @return
	 */
	public User findActiveUserByEmail(String email);
	
	/**
	 * Finds users.
	 * @param user
	 * @return
	 */
	public List<User> findUsers(User user);
	
	/**
	 * Finds user by id.
	 * @param id
	 * @return
	 */
	public User findUserById(Long id);
	
	/**
	 * Lists all roles.
	 * @return
	 */
	public List<Role> listAllRoles();
	
	/**
	 * Saves rural crosswalk.
	 * @param nv
	 * @return U or I for update or insert.
	 */
	public String saveRural(NameValue nv);
	
	/**
	 * Finds rurals.
	 * @param nv
	 * @return list of rurals
	 */
	public List<NameValue> findRurals(NameValue nv);
	
	/**
	 * Deletes rural crosswalk.
	 * @param zipCode
	 */
	public void deleteRural(String zipCode);
	
	/**
	 * Finds all utah rural zip codes.
	 * @return list of NameValue objects
	 */
	public List<NameValue> findUtahRurals();
	
	/**
	 * Saves ethnicity.
	 * @param ethnic
	 * @return
	 */
	public Ethnicity saveEthnicity(Ethnicity ethnic);
	
	/**
	 * Deletes ethnicity.
	 * @param ethnic
	 */
	public void deleteEthnicity(Ethnicity ethnic);
	
	/**
	 * Finds ethnicity by id.
	 * @param id
	 * @return
	 */
	public Ethnicity findEthnicityById(Long id);
	
	/**
	 * Finds all ethnicities.
	 * @return
	 */
	public List<Ethnicity> findAllEthnicities();
	
	/**
	 * Finds all active ethnicities.
	 * @return
	 */
	public List<Ethnicity> findAllEthnicitiesActive();
	
	/**
	 * Saves recordType.
	 * @param recordType
	 * @return
	 */
	public RecordType saveRecordType(RecordType recordType);
	
	/**
	 * Deletes recordType.
	 * @param recordTpe
	 */
	public void deleteRecordType(RecordType recordType);
	
	/**
	 * Finds recordType by id.
	 * @param id
	 * @return
	 */
	public RecordType findRecordTypeById(Long id);
	
	/**
	 * Finds all recordTypes.
	 * @return
	 */
	public List<RecordType> findAllRecordTypes();
	
	/**
	 * Finds all active recordTypes
	 * @return
	 */
	public List<RecordType> findAllRecordTypesActive();
	
	/**
	 * Saves decorationMedal.
	 * @param decorationMedal
	 * @return
	 */
	public DecorationMedal saveDecorationMedal(DecorationMedal decorationMedal);
	
	/**
	 * Deletes decorationMedal.
	 * @param decorationMedal
	 */
	public void deleteDecorationMedal(DecorationMedal decorationMedal);
	
	/**
	 * Finds decorationMedal by id.
	 * @param id
	 * @return
	 */
	public DecorationMedal findDecorationMedalById(Long id);
	
	/**
	 * Finds all decorationMedals.
	 * @return
	 */
	public List<DecorationMedal> findAllDecorationMedals();
	
	/**
	 * Finds all active decorationMedals
	 * @return
	 */
	public List<DecorationMedal> findAllDecorationMedalsActive();
	
	/**
	 * Saves relation.
	 * @param relation
	 * @return
	 */
	public Relation saveRelation(Relation relation);
	
	/**
	 * Deletes relation.
	 * @param relation
	 */
	public void deleteRelation(Relation relation);
	
	/**
	 * Finds relation by id.
	 * @param id
	 * @return
	 */
	public Relation findRelationById(Long id);
	
	/**
	 * Finds all relations.
	 * @return
	 */
	public List<Relation> findAllRelations();
	
	/**
	 * Finds all active relations
	 * @return
	 */
	public List<Relation> findAllRelationsActive();
	
	/**
	 * Saves combatZone.
	 * @param combatZone
	 * @return
	 */
	public CombatZone saveCombatZone(CombatZone combatZone);
	
	/**
	 * Deletes combatZone.
	 * @param combatZone
	 */
	public void deleteCombatZone(CombatZone combatZone);
	
	/**
	 * Finds combatZone by id.
	 * @param id
	 * @return
	 */
	public CombatZone findCombatZoneById(Long id);
	
	/**
	 * Finds all combatZones.
	 * @return
	 */
	public List<CombatZone> findAllCombatZones();
	
	/**
	 * Finds all active combatZones
	 * @return
	 */
	public List<CombatZone> findAllCombatZonesActive();
	
	/**
	 * Saves service era.
	 * @param serviceEra
	 * @return
	 */
	public ServiceEra saveServiceEra(ServiceEra serviceEra);
	
	/**
	 * Deletes service era.
	 * @param serviceEra
	 * @return
	 */
	public void deleteServiceEra(ServiceEra serviceEra);
	
	/**
	 * Find service era by id.
	 * @param id
	 * @return
	 */
	public ServiceEra findServiceEraById(Long id);

	/**
	 * Finds all service eras.
	 * @return
	 */
	public List<ServiceEra> findAllServiceEras();
	
	/**
	 * Finds all active service eras.
	 * @return
	 */
	public List<ServiceEra> findAllServiceErasActive();
	
	
	/**
	 * Saves service branch.
	 * @param serviceBranch
	 * @return
	 */
	public ServiceBranch saveServiceBranch(ServiceBranch serviceBranch);
	
	/**
	 * Deletes service branch.
	 * @param serviceBranch
	 */
	public void deleteServiceBranch(ServiceBranch serviceBranch);
	
	/**
	 * Finds service branch by id.
	 * @param id
	 * @return
	 */
	public ServiceBranch findServiceBranchById(Long id);
	
	/**
	 * Finds all service branches.
	 * @return
	 */
	public List<ServiceBranch> findAllServiceBranches();
	
	/**
	 * Finds all active service branches.
	 * @return
	 */
	public List<ServiceBranch> findAllServiceBranchesActive();
	
	/**
	 * Finds all lookup branches.
	 * @return Map object
	 */
	public Map<String, String> findLkupBranches();
	
	/**
	 * Saves discharge type.
	 * @param dischargeType
	 * @return
	 */
	public DischargeType saveDischargeType(DischargeType dischargeType);
	
	/**
	 * Deletes discharge type.
	 * @param dischargeType
	 */
	public void deleteDischargeType(DischargeType dischargeType);
	
	/**
	 * Finds discharge type by id.
	 * @param id
	 * @return
	 */
	public DischargeType findDischargeTypeById(Long id);

	/**
	 * Finds all discharge types.
	 * @return
	 */
	public List<DischargeType> findAllDischargeTypes();
	
	/**
	 * Finds all active discharge types.
	 * @return
	 */
	public List<DischargeType> findAllDischargeTypesActive();
	
	/**
	 * Saves benefit type.
	 * @param benefitType
	 * @return
	 */
	public BenefitType saveBenefitType(BenefitType benefitType);
	
	/**
	 * Deletes benefit type.
	 * @param benefitType
	 */
	public void deleteBenefitType(BenefitType benefitType);
	
	/**
	 * Finds benefit type by id.
	 * @param id
	 * @return
	 */
	public BenefitType findBenefitTypeById(Long id);

	/**
	 * Finds all benefit types.
	 * @return
	 */
	public List<BenefitType> findAllBenefitTypes();
	
	/**
	 * Finds all active benefitTypes.
	 * @return
	 */
	public List<BenefitType> findAllBenefitTypesActive();
	
	/**
	 * Saves doc type entity.
	 * @param docType
	 * @return
	 */
	public DocType saveDocType(DocType docType);
	
	/**
	 * Deletes doc type entity.
	 * @param docType
	 */
	public void deleteDocType(DocType docType);
	
	/**
	 * Finds doc type entity by id.
	 * @param id
	 * @return
	 */
	public DocType findDocTypeById(Long id);
	
	/**
	 * Finds all doc types.
	 * @return
	 */
	public List<DocType>  findAllDocTypes();
	
	/**
	 * Finds all active doc types.
	 * @return
	 */
	public List<DocType> findAllDocTypesActive();
	
	/**
	 * Saves veteran.
	 * @param veteran
	 * @return
	 */
	public Veteran saveVeteran(Veteran veteran);
	
	/**
	 * Deletes veteran.
	 * @param veteran
	 */
	public void deleteVeteran(Veteran veteran);
	
	
	/**
	 * Search veterans.
	 * 
	 * @param reportProperty
	 * @return list of 300 max veteran objects.
	 */
	public List<Veteran> findVeterans(ReportProperty reportProperty);
	
	/**
	 * Search VSO veteran by ssn.
	 * 
	 * @param ssn
	 * @return
	 */
	public List<Veteran> findVeteranVSO(String ssn);
	
	/** Search ad hoc veterans.
	 * 
	 * @param reportProperty
	 * @return
	 */
	public List<Veteran> findVeteransAdhoc(ReportProperty reportProperty);
	
	/**
	 * Gets total search veteran records.
	 * 
	 * @param reportProperty
	 * @return
	 */
	public Integer getTotalVeteransCount(ReportProperty reportProperty);
	
	/**
	 * Get total search adhoc veteran records.
	 * @param sql
	 * @return
	 */
	public Integer getTotalVeteransCountAdhoc(String sql);
	
	/**
	 * Search all veterans not limited by 300. This method is called by report action class.
	 * 
	 * @param reportProperty
	 * @return
	 */
	public List<Veteran> findVeterans_all(ReportProperty reportProperty);

	/**
	 * Search adhoc all veterans not limited by 300. This method is called by report action class.
	 * 
	 * @param reportProperty
	 * @return
	 */
	public List<Veteran> findVeteransAdhoc_all(ReportProperty reportProperty);
	
	/**
	 * Finds veterans by ssn
	 * @param ssn
	 * @return
	 */
	public List<Veteran> findVeteransBySsn(String ssn);
	
	/**
	 * Finds unreviewd veteran records.
	 * @return
	 */
	public List<Dashboard> findVeterans_unreviewed();
	
	/**
	 * Finds unreviewd veteran total records.
	 * @return
	 */
	public Integer findVeterans_unreviewed_total();
	
	/**
	 * Finds veteran by id.
	 * @param id
	 * @return
	 */
	public Veteran findVeteranById(Long id);
	
	/**
	 * Save veteran attachment.
	 * @param veteranAttachment
	 * @return
	 */
	public VeteranAttachment saveVeteranAttachment(VeteranAttachment veteranAttachment);
	
	/**
	 * Finds veteranAttachment entity by id.
	 * @param id
	 * @return
	 */
	public VeteranAttachment findVeteranAttachmentById(Long id);
	
	/**
	 * Deletes veteran attachment entity.
	 * @param entity
	 */
	public void deteteVeteranAttachment(VeteranAttachment entity);
	
	/**
	 * Gets total number of unique SSN with specified date.
	 * @param date
	 * @return
	 */
	public Long getTotalUniqueSsn(Date date);
	
	/**
	 * Gets total number of unique SSN created during specified period.
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public Long getTotalUniqueSsn(Date fromDate, Date toDate);
	
	/**
	 * Gets total number of DD 214 created during specified period.
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public Long getTotalDD214(Date fromDate, Date toDate);
	
	/**
	 * Saves veteranServicePeriod.
	 * @param veteranServicePeriod
	 * @return
	 */
	public VeteranServicePeriod saveVeteranServicePeriod(VeteranServicePeriod veteranServicePeriod);
	
	/**
	 * Deletes veteranServicePeriod.
	 * @param veteranServicePeriod
	 */
	public void deleteVeteranServicePeriod(VeteranServicePeriod veteranServicePeriod);
	
	/**
	 * Finds all veteranServicePeriod records with specified veteran id.
	 * @param veteranId
	 * @return
	 */
	public List<VeteranServicePeriod> findVeteranServicePeriods(Long veteranId);
	
	/**
	 * Deletes all veteranServicePeriods by veteran id.
	 * @param veteranId
	 */
	public void deleteVeteranServicePeriodsByVeteranId(Long veteranId);
	
	/**
	 * Deletes all veteran service period decoration medals (vsp_dm table) specified by veteranServicePeriodId.
	 * @param veteranServicePeriodId
	 */
	public void deleteVspDm(Long veteranServicePeriodId);
	
	/**
	 * Find veteranServicePeriod by id.
	 * @param veteranServicePeriodId
	 * @return
	 */
	public VeteranServicePeriod findVeteranServicePeriodById(Long veteranServicePeriodId);
	
	/**
	 * Saves note.
	 * @param note
	 * @return
	 */
	public Note saveNote(Note note);
	
	/**
	 * Deletes note.
	 * @param note
	 */
	public void deleteNote(Note note);
	
	/**
	 * Finds note entity by id.
	 * @param id
	 * @return
	 */
	public Note findNoteById(Long id);
	
	/**
	 * Saves veteranNote.
	 * @param veteranNote
	 * @return
	 */
	public VeteranNote saveVeteranNote(VeteranNote veteranNote);
	
	/**
	 * Saves VeteranBenefit.
	 * @param VeteranBenefit
	 * @return
	 */
	public VeteranBenefit saveVeteranBenefitType(VeteranBenefit veteranBenefit);
	
	/**
	 * Deletes VeteranBenefit.
	 * @param VeteranBenefit
	 */
	public void deleteVeteranBenefitType(VeteranBenefit veteranBenefit);
	
	/**
	 * Deletes VeteranBenefit records by veteran id.
	 * @param veteranId
	 */
	public void deleteVeteranBenefitByVeteranId(Long veteranId);
	
	/**
	 * Finds all VeteranBenefit records with specified veteran id.
	 * @param veteranId
	 * @return
	 */
	public List<VeteranBenefit> findVeteranBenefitTypes(Long veteranId);
	
	/**
	 * Finds VeteranBenefit record with specified veteanId and benefitTypeId
	 * @param veteranId
	 * @param benefitTypeId
	 * @return
	 */
	public VeteranBenefit findVeteranBenefitType(Long veteranId, Long benefitTypeId);
	
	/**
	 * Finds veteran_benefit ID.
	 * @param veteranId
	 * @param benefitTypeId
	 * @return
	 */
	public Long findVeteranBenefitTypeId(Long veteranId, Long benefitTypeId);
	
	/**
	 * Saves BenefitRecipient.
	 * @param BenefitRecipient
	 * @return
	 */
	public BenefitRecipient saveVeteranBenefitRecipient(BenefitRecipient benefitRecipient);
	
	/**
	 * Deletes BenefitRecipient.
	 * @param BenefitRecipient
	 */
	public void deleteVeteranBenefitRecipient(BenefitRecipient BenefitRecipient);
	
	/**
	 * Deletes BenefitRecipient records by veteranBenefitId.
	 * @param veteranBenefitId
	 */
	public void deleteVeteranBenefitRecipientsByVeteranBenefitId(Long veteranBenefitId);
	
	/**
	 * Finds all BenefitRecipient records with specified veteranBenefitId.
	 * @param veteranBenefitId
	 * @return
	 */
	public List<BenefitRecipient> findVeteranBenefitRecipients(Long veteranBenefitId);

	/**
	 * Saves report entity.
	 * @param report
	 * @return
	 */
	public Report saveReport(Report report);
	
	/**
	 * Finds report entity by id.
	 * @param reportId
	 * @return
	 */
	public Report findReportById(Long reportId);
	
	/**
	 * Finds all reports by specified user id.
	 * @param userId
	 * @return
	 */
	public List<Report> findReportsByUserId(Long userId);
	
	/**
	 * Finds all sharable reports.
	 * @return
	 */
	public List<Report> findReportsBySharable();
	
	/**
	 * Deletes report entity.
	 * @param report
	 */
	public void deleteReport(Report report);
	
	/**
	 * Saves reportProperty entity.
	 * @param reportProperty
	 * @return
	 */
	public ReportProperty saveReportProperty(ReportProperty reportProperty);
	
	/**
	 * Deletes reportProperty entity.
	 * @param reportProperty
	 */
	public void deleteReportProperty(ReportProperty reportProperty);
	
	/**
	 * Save dataImport entity.
	 * @param dataImport
	 * @return
	 */
	public DataImport saveDataImport(DataImport dataImport);
	
	/**
	 * Deletes dataImport entity.
	 * @param dataImport
	 */
	public void deleteDataImport(DataImport dataImport);
	
	/** 
	 * Finds data import entity by id.
	 * @param dataImportId
	 */
	public DataImport findDataImportById(Long dataImportId);
	
	/**
	 * Finds dataImport entities by source id.
	 * @param sourceId
	 * @return
	 */
	public List<DataImport> findDataImportsBySource(Long sourceId);
	
	/**
	 * Finds all registration records that have not been imported.
	 * @return list of Registration objects.
	 */
	public List<Registration> findRegisteredRecords();
	
	/**
	 * Saves registration entity.
	 * @param reg
	 * @return Registration object
	 */
	public Registration saveRegistration(Registration reg);
	
	/**
	 * Finds all veterans by zip codes.
	 * @param zipCodes
	 * @return list
	 */
	public List<Veteran> findVeteransByZipCodes(String zipCodes);
	
	/**
	 * Finds current veterans with email by sources and modified date range.
	 * @param sources
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public List<Veteran> findVeteransBySED(Long[] sources, Date fromDate, Date toDate);

	/**
	 * Finds doc type name by ID.
	 * 
	 * @param id
	 * @return
	 */
	public String findDocNameById(Long id);
	
	/**
	 * Finds directory and file counts from attachment_pointer_info table.
	 * @return map object.
	 */
	public Map<String, Long> findDirFileCount();
	
	/**
	 * Inserts first record into attachment_pointer_info table.
	 * @param dfc - a map object of values to be inserted.
	 */
	public void saveDirFileCount(Map<String, Long> dfc);
	
	/**
	 * Updates first record of attachment_pointer_info table.
	 * @param dfc - a map object of values to be updated.
	 */
	public void updateDirFileCount(Map<String, Long> dfc);
	
	/**
	 * Finds entity by name.
	 * @param name
	 * @return
	 */
	public Ethnicity findEthnicityByName(String name);
	
	/**
	 * Finds entity by name.
	 * @param name
	 * @return
	 */
	public ServiceEra findServiceEraByName(String name);
	
	/**
	 * Finds entity by name.
	 * @param name
	 * @return
	 */
	public ServiceBranch findServiceBranchByName(String name);
	
	/**
	 * Finds entity by name.
	 * @param name
	 * @return
	 */
	public DischargeType findDischargeTypeByName(String name);

	/**
	 * Finds entity by name.
	 * @param name
	 * @return
	 */
	public BenefitType findBenefitTypeByName(String name);
	
	/**
	 * Finds entity by name.
	 * @param name
	 * @return
	 */
	public DocType findDocTypeByName(String name);
	
	/**
	 * Finds entity by name.
	 * @param name
	 * @return
	 */
	public RecordType findRecordTypeByName(String name);
	
	/**
	 * Finds entity by name.
	 * @param name
	 * @return
	 */
	public Relation findRelationByName(String name);
	
	/**
	 * Finds entity by name.
	 * @param name
	 * @return
	 */
	public CombatZone findCombatZoneByName(String name);
	
	/**
	 * Finds entity by name.
	 * @param name
	 * @return
	 */
	public DecorationMedal findDecorationMedalByName(String name);
	
	/**
	 * Finds entity by name.
	 * @param name
	 * @return
	 */
	public NameValue findRuralByName(String name);
}
