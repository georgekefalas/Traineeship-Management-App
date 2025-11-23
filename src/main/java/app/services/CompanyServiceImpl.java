package app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.*;
import app.mappers.*;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CompanyServiceImpl implements CompanyService{
	
	
	@Autowired
	private CompanyMapper compMapper;
	
	@Autowired
	private EvaluationMapper evalMapper;
	
	@Autowired
	private TraineeshipPositionMapper trainMapper;
	
	
	public CompanyServiceImpl() {
		
	}
	
	@Autowired
	public CompanyServiceImpl(CompanyMapper compMapper) {
		this.compMapper = compMapper;
	}
	
	
	@Override
	public void createProfile(Company company) {	
		compMapper.save(company);
	}
	
	
	@Override
	public Company retrieveProfile(String username) {
		
		Optional<Company> comp = compMapper.findById(username);
		
		if (comp.isPresent())
			return comp.get();
		else
			return null;
	}
	
	
	@Override
	public List<TraineeshipPosition> showAvailablePositions(String username) {
		
		Optional<Company> comp = compMapper.findById(username);
		
		
		if (comp.isPresent()) 
		{
			 List<TraineeshipPosition> allPositions = comp.get().getPositions();

		     List<TraineeshipPosition> availablePositions = allPositions.stream()
		            .filter(pos -> !pos.isAssigned())
		            .collect(Collectors.toList());

		    return availablePositions;
		}
		else
			throw new RuntimeException("Company" + username + "not found" );		
	}
	
	
	@Override
	public List<TraineeshipPosition> showAssignedPositions(String username) {
		
		Optional<Company> comp = compMapper.findById(username);
		List<TraineeshipPosition> pos = null; 
		
		if (comp.isPresent()) 
		{
			pos = comp.get().getAssignedPositions();	 
			return pos;
		}
		else
			throw new RuntimeException("Company" + username + "not found" );		
	}
	
	
	@Override
	public void evaluateTraineeship(Integer positionID, Evaluation evaluation) {
		
		Optional<TraineeshipPosition> pos = trainMapper.findById(positionID);
		List<Evaluation> evals = null;
				
		if (pos.isPresent())
		{
			evals = pos.get().getEvaluations();
			evals.add(evaluation);
			pos.get().setEvaluations(evals);
			evalMapper.save(evaluation);
		}
		else
			throw new RuntimeException("Traineeship not found" );
	}
	
	
	public void addPosition(String username,TraineeshipPosition position) {
		
		Optional<Company> comp = compMapper.findById(username);
		
		if (!comp.isPresent()) {
				throw new RuntimeException("Company does not exist");
		}
		
		position.setCompany(comp.get());
		
		TraineeshipPosition pos= trainMapper.save(position);

			
		comp.get().addPosition(pos);
		
		compMapper.save(comp.get());
	
	}
		
		
	public void deletePosition (String username, Integer positionID) {
		
		Optional<TraineeshipPosition> pos = trainMapper.findById(positionID);
		Optional<Company> comp = compMapper.findById(username);
		
		if(pos.isPresent()&& comp.isPresent()) { 
			comp.get().deletePositon(pos.get());
			compMapper.save(comp.get());
			
			trainMapper.deleteById(positionID);
		}
		else
			throw new RuntimeException("Traineeship not found" );
		
		
	}
	
	
	public TraineeshipPosition showDetails(Integer positionID) {
		
		Optional<TraineeshipPosition> pos = trainMapper.findById(positionID);
		
		if(pos.isPresent()) {
			return pos.get();
		}
		else
			throw new RuntimeException("Details not found" );
	}
	
	
	@Override
    public Optional<Company> findByUsername(String username) {
        return compMapper.findByUsername(username);
    }
	
	
}