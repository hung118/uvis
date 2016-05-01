package gov.utah.va.vts.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.utah.va.vts.dao.BenefitRecipientDao;
import gov.utah.va.vts.dao.BenefitTypeDao;
import gov.utah.va.vts.dao.CombatZoneDao;
import gov.utah.va.vts.dao.DataImportDao;
import gov.utah.va.vts.dao.DecorationMedalDao;
import gov.utah.va.vts.dao.DischargeTypeDao;
import gov.utah.va.vts.dao.DocTypeDao;
import gov.utah.va.vts.dao.EthnicityDao;
import gov.utah.va.vts.dao.NoteDao;
import gov.utah.va.vts.dao.RecordTypeDao;
import gov.utah.va.vts.dao.RegistrationDao;
import gov.utah.va.vts.dao.RelationDao;
import gov.utah.va.vts.dao.ReportDao;
import gov.utah.va.vts.dao.ReportPropertyDao;
import gov.utah.va.vts.dao.RoleDao;
import gov.utah.va.vts.dao.ServiceBranchDao;
import gov.utah.va.vts.dao.ServiceEraDao;
import gov.utah.va.vts.dao.UserDao;
import gov.utah.va.vts.dao.VeteranAttachmentDao;
import gov.utah.va.vts.dao.VeteranBenefitDao;
import gov.utah.va.vts.dao.VeteranDao;
import gov.utah.va.vts.dao.VeteranNoteDao;
import gov.utah.va.vts.dao.VeteranServicePeriodDao;
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
import gov.utah.va.vts.service.VtsService;

/**
 * VTS service implementation class.
 * 
 * @author hnguyen
 *
 */
@Service(value = "vtsService")
@Transactional
public class VtsServiceImpl implements VtsService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private EthnicityDao ethnicityDao;
	@Autowired
	private ServiceEraDao serviceEraDao;
	@Autowired
	private ServiceBranchDao serviceBranchDao;
	@Autowired
	private DischargeTypeDao dischargeTypeDao;
	@Autowired
	private BenefitTypeDao benefitTypeDao;
	@Autowired
	private VeteranDao veteranDao;
	@Autowired
	private VeteranServicePeriodDao veteranServicePeriodDao;
	@Autowired
	private NoteDao noteDao;
	@Autowired
	private VeteranNoteDao veteranNoteDao;
	@Autowired
	private RecordTypeDao recordTypeDao;
	@Autowired
	private RelationDao relationDao;
	@Autowired
	private CombatZoneDao combatZoneDao;
	@Autowired
	private DecorationMedalDao decorationMedalDao;
	@Autowired
	private VeteranBenefitDao veteranBenefitDao;
	@Autowired
	private BenefitRecipientDao benefitRecipientDao;
	@Autowired
	private ReportDao reportDao;
	@Autowired
	private ReportPropertyDao reportPropertyDao;
	@Autowired
	private DataImportDao dataImportDao;
	@Autowired
	private VeteranAttachmentDao veteranAttachmentDao;
	@Autowired
	private DocTypeDao docTypeDao;
	@Autowired
	private RegistrationDao registrationDao;
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#flush()
	 */
	public void flush() {
		userDao.flush();
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#clear()
	 */
	public void clear() {
		userDao.clear();
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveUser(gov.utah.va.vts.model.User)
	 */
	@Override
	public User saveUser(User user) {
		return userDao.save(user);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteUser(gov.utah.va.vts.model.User)
	 */
	@Override
	public void deleteUser(User user) {
		userDao.delete(user);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findUserByEmail(java.lang.String)
	 */
	@Override
	public User findUserByEmail(String email) {
		return userDao.findUserByEmail(email);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findActiveUserByEmail(java.lang.String)
	 */
	public User findActiveUserByEmail(String email) {
		return userDao.findActiveUserByEmail(email);
	}	

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findUsers(gov.utah.va.vts.model.User)
	 */
	@Override
	public List<User> findUsers(User user) {
		return userDao.findUsers(user);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#listAllRoles()
	 */
	@Override
	public List<Role> listAllRoles() {
		return roleDao.listAll("roleName");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findUserById(java.lang.Long)
	 */
	@Override
	public User findUserById(Long id) {
		return userDao.findById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveRural(gov.utah.va.vts.model.NameValue)
	 */
	public String saveRural(NameValue nv) {
		return userDao.saveRural(nv);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findRurals(gov.utah.va.vts.model.NameValue)
	 */
	public List<NameValue> findRurals(NameValue nv) {
		return userDao.findRurals(nv);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteRural(java.lang.String)
	 */
	public void deleteRural(String zipCode) {
		userDao.deleteRural(zipCode);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findUtahRurals()
	 */
	public List<NameValue> findUtahRurals() {
		return userDao.findUtahRurals();
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveEthnicity(gov.utah.va.vts.model.Ethnicity)
	 */
	@Override
	public Ethnicity saveEthnicity(Ethnicity ethnic) {
		return ethnicityDao.save(ethnic);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteEthnicity(gov.utah.va.vts.model.Ethnicity)
	 */
	@Override
	public void deleteEthnicity(Ethnicity ethnic) {
		ethnicityDao.delete(ethnic);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findEthnicityById(java.lang.Long)
	 */
	@Override
	public Ethnicity findEthnicityById(Long id) {
		return ethnicityDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllEthnicities()
	 */
	@Override
	public List<Ethnicity> findAllEthnicities() {
		return ethnicityDao.listAll("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveServiceEra(gov.utah.va.vts.model.ServiceEra)
	 */
	@Override
	public ServiceEra saveServiceEra(ServiceEra serviceEra) {
		return serviceEraDao.save(serviceEra);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteServiceEra(gov.utah.va.vts.model.ServiceEra)
	 */
	@Override
	public void deleteServiceEra(ServiceEra serviceEra) {
		serviceEraDao.delete(serviceEra);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findServiceEraById(java.lang.Long)
	 */
	@Override
	public ServiceEra findServiceEraById(Long id) {
		return serviceEraDao.findById(id);
	}

	@Override
	public List<ServiceEra> findAllServiceEras() {
		return serviceEraDao.listAll("startDate");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveServiceBranch(gov.utah.va.vts.model.ServiceBranch)
	 */
	@Override
	public ServiceBranch saveServiceBranch(ServiceBranch serviceBranch) {
		return serviceBranchDao.save(serviceBranch);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteServiceBranch(gov.utah.va.vts.model.ServiceBranch)
	 */
	@Override
	public void deleteServiceBranch(ServiceBranch serviceBranch) {
		serviceBranchDao.delete(serviceBranch);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findServiceBranch(java.lang.Long)
	 */
	@Override
	public ServiceBranch findServiceBranchById(Long id) {
		return serviceBranchDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllServiceBranches()
	 */
	@Override
	public List<ServiceBranch> findAllServiceBranches() {
		return serviceBranchDao.listAll("name");
	}
	
	/**
	 * Finds all lookup branches.
	 * @return Map object
	 */
	public Map<String, String> findLkupBranches() {
		return serviceBranchDao.findLkupBranches();
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveDischargeType(gov.utah.va.vts.model.DischargeType)
	 */
	@Override
	public DischargeType saveDischargeType(DischargeType dischargeType) {
		return dischargeTypeDao.save(dischargeType);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteDischargeType(gov.utah.va.vts.model.DischargeType)
	 */
	@Override
	public void deleteDischargeType(DischargeType dischargeType) {
		dischargeTypeDao.delete(dischargeType);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findDischargeTypeById(java.lang.Long)
	 */
	@Override
	public DischargeType findDischargeTypeById(Long id) {
		return dischargeTypeDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllDischargeTypes()
	 */
	@Override
	public List<DischargeType> findAllDischargeTypes() {
		return dischargeTypeDao.listAll("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveBenefitType(gov.utah.va.vts.model.BenefitType)
	 */
	@Override
	public BenefitType saveBenefitType(BenefitType benefitType) {
		return benefitTypeDao.save(benefitType);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteBenefitType(gov.utah.va.vts.model.BenefitType)
	 */
	@Override
	public void deleteBenefitType(BenefitType benefitType) {
		benefitTypeDao.delete(benefitType);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findBenefitTypeById(java.lang.Long)
	 */
	@Override
	public BenefitType findBenefitTypeById(Long id) {
		return benefitTypeDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllBenefitTypes()
	 */
	@Override
	public List<BenefitType> findAllBenefitTypes() {
		return benefitTypeDao.listAll("name");
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllBenefitTypesActive()
	 */
	@Override
	public List<BenefitType> findAllBenefitTypesActive() {
		return benefitTypeDao.listAllActive("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveVeteran(gov.utah.va.vts.model.Veteran)
	 */
	@Override
	public Veteran saveVeteran(Veteran veteran) {
		return veteranDao.save(veteran);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteVeteran(gov.utah.va.vts.model.Veteran)
	 */
	@Override
	public void deleteVeteran(Veteran veteran) {
		veteranDao.delete(veteran);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeterans(gov.utah.va.vts.model.ReportProperty)
	 */
	public List<Veteran> findVeterans(ReportProperty reportProperty) {
		return veteranDao.findVeterans(reportProperty);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeterans(gov.utah.va.vts.model.Veteran)
	 */
	public Integer getTotalVeteransCount(ReportProperty reportProperty) {
		return veteranDao.getTotalVeteransCount(reportProperty);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#getTotalVeteransCountAdhoc(java.lang.String)
	 */
	public Integer getTotalVeteransCountAdhoc(String sql) {
		return veteranDao.getTotalVeteransCountAdhoc(sql);
	}
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeterans_all(gov.utah.va.vts.model.ReportProperty)
	 */
	public List<Veteran> findVeterans_all(ReportProperty reportProperty) {
		return veteranDao.findVeterans_all(reportProperty);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeteransAdhoc_all(gov.utah.va.vts.model.ReportProperty)
	 */
	public List<Veteran> findVeteransAdhoc_all(ReportProperty reportProperty) {
		return veteranDao.findVeteransAdhoc_all(reportProperty);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeteransBySsn(java.lang.String)
	 */
	@Override
	public List<Veteran> findVeteransBySsn(String ssn) {
		return veteranDao.findVeteransBySsn(ssn);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeteranVSO(java.lang.String)
	 */
	public List<Veteran> findVeteranVSO(String ssn) {
		return veteranDao.findVeteranVSO(ssn);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeteransAdhoc(gov.utah.va.vts.model.ReportProperty)
	 */
	public List<Veteran> findVeteransAdhoc(ReportProperty reportProperty) {
		return veteranDao.findVeteransAdhoc(reportProperty);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeterans_unreviewed()
	 */
	public List<Dashboard> findVeterans_unreviewed() {
		return veteranDao.findVeterans_unreviewed();
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeterans_unreviewed_total()
	 */
	public Integer findVeterans_unreviewed_total() {
		return veteranDao.findVeterans_unreviewed_total();
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeteranById(java.lang.Long)
	 */
	@Override
	public Veteran findVeteranById(Long id) {
		return veteranDao.findById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveVeteranAttachment(gov.utah.va.vts.model.VeteranAttachments)
	 */
	public VeteranAttachment saveVeteranAttachment(VeteranAttachment veteranAttachment) {
		return veteranAttachmentDao.save(veteranAttachment);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeteranAttachmentById(java.lang.Long)
	 */
	public VeteranAttachment findVeteranAttachmentById(Long id) {
		return veteranAttachmentDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deteteVeteranAttachment(gov.utah.va.vts.model.VeteranAttachments)
	 */
	public void deteteVeteranAttachment(VeteranAttachment entity) {
		veteranAttachmentDao.delete(entity);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#getTotalUniqueSsn(java.util.Date)
	 */
	@Override
	public Long getTotalUniqueSsn(Date date) {
		return veteranDao.getTotalUniqueSsn(date);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#getTotalUniqueSsn(java.util.Date, java.util.Date)
	 */
	@Override
	public Long getTotalUniqueSsn(Date fromDate, Date toDate) {
		return veteranDao.getTotalUniqueSsn(fromDate, toDate);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#getTotalDD214(java.util.Date, java.util.Date)
	 */
	public Long getTotalDD214(Date fromDate, Date toDate) {
		return veteranDao.getTotalDD214(fromDate, toDate);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#savVeteranServicePeriod(gov.utah.va.vts.model.VeteranServicePeriod)
	 */
	@Override
	public VeteranServicePeriod saveVeteranServicePeriod(
			VeteranServicePeriod veteranServicePeriod) {
		return veteranServicePeriodDao.save(veteranServicePeriod);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteVeteranServicePeriod(gov.utah.va.vts.model.VeteranServicePeriod)
	 */
	@Override
	public void deleteVeteranServicePeriod(
			VeteranServicePeriod veteranServicePeriod) {
		veteranServicePeriodDao.delete(veteranServicePeriod);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeteranServicePeriod(java.lang.Long)
	 */
	@Override
	public List<VeteranServicePeriod> findVeteranServicePeriods(Long veteranId) {
		return veteranServicePeriodDao.findVeteranServicePeriods(veteranId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteVeteranServicePeriodsByVeteranId(java.lang.Long)
	 */
	@Override
	public void deleteVeteranServicePeriodsByVeteranId(Long veteranId) {
		veteranServicePeriodDao.deleteVeteranServicePeriodsByVeteranId(veteranId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteVspDm(java.lang.Long)
	 */
	public void deleteVspDm(Long veteranServicePeriodId) {
		veteranServicePeriodDao.deleteVspDm(veteranServicePeriodId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeteranServicePeriodById(java.lang.Long)
	 */
	@Override
	public VeteranServicePeriod findVeteranServicePeriodById(
			Long veteranServicePeriodId) {
		return veteranServicePeriodDao.findById(veteranServicePeriodId);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveNotes(gov.utah.va.vts.model.Notes)
	 */
	@Override
	public Note saveNote(Note note) {
		return noteDao.save(note);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findNoteById(java.lang.Long)
	 */
	@Override
	public Note findNoteById(Long id) {
		return noteDao.findById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveVeteranNote(gov.utah.va.vts.model.VeteranNote)
	 */
	@Override
	public VeteranNote saveVeteranNote(VeteranNote veteranNote) {
		return veteranNoteDao.save(veteranNote);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveRecordType(gov.utah.va.vts.model.RecordType)
	 */
	@Override
	public RecordType saveRecordType(RecordType recordType) {
		return recordTypeDao.save(recordType);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteRecordType(gov.utah.va.vts.model.RecordType)
	 */
	@Override
	public void deleteRecordType(RecordType recordType) {
		recordTypeDao.delete(recordType);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findRecordTypeById(java.lang.Long)
	 */
	@Override
	public RecordType findRecordTypeById(Long id) {
		return recordTypeDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllRecordTypes()
	 */
	@Override
	public List<RecordType> findAllRecordTypes() {
		return recordTypeDao.listAll("name");
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllRecordTypesActive()
	 */
	public List<RecordType> findAllRecordTypesActive() {
		return recordTypeDao.listAllActive("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllEthnicitiesActive()
	 */
	@Override
	public List<Ethnicity> findAllEthnicitiesActive() {
		return ethnicityDao.listAllActive("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllServiceErasActive()
	 */
	@Override
	public List<ServiceEra> findAllServiceErasActive() {
		return serviceEraDao.listAllActive("startDate");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllServiceBranchesActive()
	 */
	@Override
	public List<ServiceBranch> findAllServiceBranchesActive() {
		return serviceBranchDao.listAllActive("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllDischargeTypesActive()
	 */
	@Override
	public List<DischargeType> findAllDischargeTypesActive() {
		return dischargeTypeDao.listAllActive("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deletNote(gov.utah.va.vts.model.Note)
	 */
	@Override
	public void deleteNote(Note note) {
		noteDao.delete(note);
		
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveRelation(gov.utah.va.vts.model.Relation)
	 */
	@Override
	public Relation saveRelation(Relation relation) {
		return relationDao.save(relation);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteRelation(gov.utah.va.vts.model.Relation)
	 */
	@Override
	public void deleteRelation(Relation relation) {
		relationDao.delete(relation);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findRelationById(java.lang.Long)
	 */
	@Override
	public Relation findRelationById(Long id) {
		return relationDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllRelations()
	 */
	@Override
	public List<Relation> findAllRelations() {
		return relationDao.listAll("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllRelationsActive()
	 */
	@Override
	public List<Relation> findAllRelationsActive() {
		return relationDao.listAllActive("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveCombatZone(gov.utah.va.vts.model.CombatZone)
	 */
	@Override
	public CombatZone saveCombatZone(CombatZone combatZone) {
		return combatZoneDao.save(combatZone);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteCombatZone(gov.utah.va.vts.model.CombatZone)
	 */
	@Override
	public void deleteCombatZone(CombatZone combatZone) {
		combatZoneDao.delete(combatZone);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findCombatZoneById(java.lang.Long)
	 */
	@Override
	public CombatZone findCombatZoneById(Long id) {
		return combatZoneDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllCombatZones()
	 */
	@Override
	public List<CombatZone> findAllCombatZones() {
		return combatZoneDao.listAll("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllCombatZonesActive()
	 */
	@Override
	public List<CombatZone> findAllCombatZonesActive() {
		return combatZoneDao.listAllActive("name");
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveDecorationMedal(gov.utah.va.vts.model.DecorationMedal)
	 */
	@Override
	public DecorationMedal saveDecorationMedal(DecorationMedal decorationMedal) {
		return decorationMedalDao.save(decorationMedal);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteDecorationMedal(gov.utah.va.vts.model.DecorationMedal)
	 */
	@Override
	public void deleteDecorationMedal(DecorationMedal decorationMedal) {
		decorationMedalDao.delete(decorationMedal);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findDecorationMedalById(java.lang.Long)
	 */
	@Override
	public DecorationMedal findDecorationMedalById(Long id) {
		return decorationMedalDao.findById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllDecorationMedals()
	 */
	@Override
	public List<DecorationMedal> findAllDecorationMedals() {
		return decorationMedalDao.listAll("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllDecorationMedalsActive()
	 */
	@Override
	public List<DecorationMedal> findAllDecorationMedalsActive() {
		return decorationMedalDao.listAllActive("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveVeteranBenefitType(gov.utah.va.vts.model.VeteranBenefit)
	 */
	@Override
	public VeteranBenefit saveVeteranBenefitType(VeteranBenefit veteranBenefit) {
		return veteranBenefitDao.save(veteranBenefit);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteVeteranBenefitType(gov.utah.va.vts.model.VeteranBenefit)
	 */
	@Override
	public void deleteVeteranBenefitType(VeteranBenefit veteranBenefit) {
		veteranBenefitDao.delete(veteranBenefit);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteVeteranBenefitByVeteranId(java.lang.Long)
	 */
	@Override
	public void deleteVeteranBenefitByVeteranId(Long veteranId) {
		veteranBenefitDao.deleteVeteranBenefitByVeteranId(veteranId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeteranBenefitTypes(java.lang.Long)
	 */
	@Override
	public List<VeteranBenefit> findVeteranBenefitTypes(Long veteranId) {
		return veteranBenefitDao.findVeteranBenefits(veteranId);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeteranBenefitType(java.lang.Long, java.lang.Long)
	 */
	@Override
	public VeteranBenefit findVeteranBenefitType(Long veteranId, Long benefitTypeId) {
		return veteranBenefitDao.findVeteranBenefit(veteranId, benefitTypeId);
	}

	public Long findVeteranBenefitTypeId(Long veteranId, Long benefitTypeId) {
		return veteranBenefitDao.findVeteranBenefitId(veteranId, benefitTypeId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveVeteranBenefitRecipient(gov.utah.va.vts.model.BenefitRecipient)
	 */
	@Override
	public BenefitRecipient saveVeteranBenefitRecipient(
			BenefitRecipient BenefitRecipient) {
		return benefitRecipientDao.save(BenefitRecipient);
	}

	@Override
	public void deleteVeteranBenefitRecipient(BenefitRecipient BenefitRecipient) {
		benefitRecipientDao.delete(BenefitRecipient);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeteranBenefitRecipients(java.lang.Long)
	 */
	@Override
	public List<BenefitRecipient> findVeteranBenefitRecipients(Long veteranBenefitId) {
		return benefitRecipientDao.findVeteranBenefitRecipients(veteranBenefitId);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteVeteranBenefitRecipientsByVeteranBenefitId(java.lang.Long)
	 */
	@Override
	public void deleteVeteranBenefitRecipientsByVeteranBenefitId(
			Long veteranBenefitId) {
		benefitRecipientDao.deleteBenefitRecipientsByVeteranBenefitId(veteranBenefitId);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveReport(gov.utah.va.vts.model.Report)
	 */
	@Override
	public Report saveReport(Report report) {
		return reportDao.save(report);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteReport(gov.utah.va.vts.model.Report)
	 */
	@Override
	public void deleteReport(Report report) {
		reportDao.delete(report);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findReportById(java.lang.Long)
	 */
	@Override
	public Report findReportById(Long reportId) {
		return reportDao.findById(reportId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findReportsByUserId(java.lang.Long)
	 */
	@Override
	public List<Report> findReportsByUserId(Long userId) {
		return reportDao.findReportsByUserId(userId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findReportsBySharable()
	 */
	public List<Report> findReportsBySharable() {
		return reportDao.findReportsBySharable();
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveReportProperty(gov.utah.va.vts.model.ReportProperty)
	 */
	@Override
	public ReportProperty saveReportProperty(ReportProperty reportProperty) {
		return reportPropertyDao.save(reportProperty);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteReportProperty(gov.utah.va.vts.model.ReportProperty)
	 */
	@Override
	public void deleteReportProperty(ReportProperty reportProperty) {
		reportPropertyDao.delete(reportProperty);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveDataImport(gov.utah.va.vts.model.DataImport)
	 */
	@Override
	public DataImport saveDataImport(DataImport dataImport) {
		return dataImportDao.save(dataImport);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteDataImport(gov.utah.va.vts.model.DataImport)
	 */
	@Override
	public void deleteDataImport(DataImport dataImport) {
		dataImportDao.delete(dataImport);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findDataImportById(java.lang.Long)
	 */
	@Override
	public DataImport findDataImportById(Long dataImportId) {
		return dataImportDao.findById(dataImportId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findDataImportsBySource(java.lang.Long)
	 */
	@Override
	public List<DataImport> findDataImportsBySource(Long sourceId) {
		return dataImportDao.findDataImportsBySource(sourceId);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveDocType(gov.utah.va.vts.model.DocType)
	 */
	@Override
	public DocType saveDocType(DocType docType) {
		return docTypeDao.save(docType);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#deleteDocType(gov.utah.va.vts.model.DocType)
	 */
	@Override
	public void deleteDocType(DocType docType) {
		docTypeDao.delete(docType);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findDocTypeById(java.lang.Long)
	 */
	@Override
	public DocType findDocTypeById(Long id) {
		return docTypeDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllDocTypes()
	 */
	@Override
	public List<DocType> findAllDocTypes() {
		return docTypeDao.listAll("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findAllDocTypesActive()
	 */
	@Override
	public List<DocType> findAllDocTypesActive() {
		return docTypeDao.listAllActive("name");
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findRegisteredRecords()
	 */
	@Override
	public List<Registration> findRegisteredRecords() {
		return registrationDao.findRegisteredRecords();
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveRegistration(gov.utah.va.vts.model.Registration)
	 */
	@Override
	public Registration saveRegistration(Registration reg) {
		return registrationDao.save(reg);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeteransByZipCodes(java.lang.String)
	 */
	@Override
	public List<Veteran> findVeteransByZipCodes(String zipCodes) {
		return veteranDao.findVeteransByZipCodes(zipCodes);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findVeteransBySED(java.lang.Long[], java.util.Date, java.util.Date)
	 */
	public List<Veteran> findVeteransBySED(Long[] sources, Date fromDate, Date toDate) {
		return veteranDao.findVeteransBySED(sources, fromDate, toDate);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findDocNameById(java.lang.Long)
	 */
	@Override
	public String findDocNameById(Long id) {
		return docTypeDao.findDocNameById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findDirFileCount()
	 */
	@Override
	public Map<String, Long> findDirFileCount() {
		return userDao.findDirFileCount();
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#saveDirFileCount(java.util.Map)
	 */
	@Override
	public void saveDirFileCount(Map<String, Long> dfc) {
		userDao.saveDirFileCount(dfc);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#updateDirFileCount(java.util.Map)
	 */
	@Override
	public void updateDirFileCount(Map<String, Long> dfc) {
		userDao.updateDirFileCount(dfc);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.service.VtsService#findEthnicityByName(java.lang.String)
	 */
	public Ethnicity findEthnicityByName(String name) {
		return ethnicityDao.findByName(name);
	}
	
	public ServiceEra findServiceEraByName(String name) {
		return serviceEraDao.findByName(name);
	}

	public ServiceBranch findServiceBranchByName(String name) {
		return serviceBranchDao.findByName(name);
	}
	
	public DischargeType findDischargeTypeByName(String name) {
		return dischargeTypeDao.findByName(name);
	}

	@Override
	public BenefitType findBenefitTypeByName(String name) {
		return benefitTypeDao.findByName(name);
	}

	@Override
	public DocType findDocTypeByName(String name) {
		return docTypeDao.findByName(name);
	}

	@Override
	public RecordType findRecordTypeByName(String name) {
		return recordTypeDao.findByName(name);
	}

	@Override
	public Relation findRelationByName(String name) {
		return relationDao.findByName(name);
	}

	@Override
	public CombatZone findCombatZoneByName(String name) {
		return combatZoneDao.findByName(name);	
	}

	@Override
	public DecorationMedal findDecorationMedalByName(String name) {
		return decorationMedalDao.findByName(name);		
	}

	@Override
	public NameValue findRuralByName(String name) {
		return userDao.findRuralByName(name);	
	}

}
