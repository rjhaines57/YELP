package logparserweb;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface LogFileDao extends CrudRepository<LogFile, Long> {
	
	public LogFile findByOriginalFileName(String originalFileName);
	public LogFile findById(Long id);
	//public ArrayList<LogFile> getAllByOriginalFileName();
		
	}
	
