package net.sf.xplanner.hibernate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.BasicTransformerAdapter;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * Result transformer that allows to transform a result to
 * a user specified class which will be populated via setter
 * methods or fields matching the alias names.
 * <p/>
 * <pre>
 * List resultWithAliasedBean = s.createCriteria(Enrolment.class)
 * 			.createAlias("student", "st")
 * 			.createAlias("course", "co")
 * 			.setProjection( Projections.projectionList()
 * 					.add( Projections.property("co.description"), "courseDescription" )
 * 			)
 * 			.setResultTransformer( new AliasToBeanResultTransformer(StudentDTO.class) )
 * 			.list();
 * <p/>
 *  StudentDTO dto = (StudentDTO)resultWithAliasedBean.get(0);
 * 	</pre>
 *
 * @author max
 */
public class AliasToBeanResultTransformer<T extends Identifiable> extends BasicTransformerAdapter {

	// IMPL NOTE : due to the delayed population of setters (setters cached
	// 		for performance), we really cannot pro0perly define equality for
	// 		this transformer

	private final Class<T> resultClass;
	private final PropertyAccessor propertyAccessor;
	private Setter[] setters;
	private List<String> subentities = new ArrayList<String>();
	private String[] aliases = null;
	private int indexOfId;

	public AliasToBeanResultTransformer(Class<T> resultClass) {
		if ( resultClass == null ) {
			throw new IllegalArgumentException( "resultClass cannot be null" );
		}
		this.resultClass = resultClass;
		propertyAccessor = new ChainedPropertyAccessor(
				new PropertyAccessor[] {
						PropertyAccessorFactory.getPropertyAccessor( resultClass, null ),
						PropertyAccessorFactory.getPropertyAccessor( "field" )
				}
		);
	} 

	public Object transformTuple(Object[] tuple, String[] aliases) {
		if(this.aliases == null) {
			this.aliases = aliases;
		}
		return tuple;
	}

	@Override
	@SuppressWarnings({ "unchecked", "boxing" })
	public List transformList(List collection) {
		Map<Integer,T> map = new HashMap<Integer,T>();

		if (setters == null && aliases != null) {
			setters = new Setter[aliases.length];
			for (int i = 0; i < aliases.length; i++) {
				String alias = aliases[i];
				if (alias != null) {
					if (!alias.contains(".")) {
						setters[i] = propertyAccessor.getSetter(resultClass, alias);
						if("id".equals(alias)) {
							indexOfId = i;
						}
					}else {
						String[] path = alias.split("\\.");
						if(path.length==2) {
							String subEntityName = path[0];
							if(subentities.contains(subEntityName)) {
								setters[i] = new ObjectSetter(propertyAccessor.getGetter(resultClass, subEntityName), path[1], false);
							}else{
								setters[i] = new ObjectSetter(propertyAccessor.getGetter(resultClass, subEntityName), path[1], true);
								subentities.add(subEntityName);
							}
						}
					}
				}
			}
		}
		
		for (Object[] tuple : (List<Object[]>) collection) {
			T result;
			try {
				Integer id = (Integer) tuple[indexOfId];
				if(map.containsKey(id)) {
					result = map.get(id);
				}else {
					result = resultClass.newInstance();
					map.put(id, result);
				}

				for (int i = 0; i < aliases.length; i++) {
					if(tuple[i]!=null){
						setters[i].set(result, tuple[i], null);
					}
				}
			} catch (InstantiationException e) {
				throw new HibernateException(
						"Could not instantiate resultclass: "
								+ resultClass.getName());
			} catch (IllegalAccessException e) {
				throw new HibernateException(
						"Could not instantiate resultclass: "
								+ resultClass.getName());
			}
		}
		return new ArrayList<Identifiable>(map.values());

	}

	private Type getType(String[] path) {
		try {
			Field field = resultClass.getDeclaredField(path[0]);
			Type genericType = field.getGenericType();
			if(genericType instanceof ParameterizedType) {
				ParameterizedType type = (ParameterizedType) genericType;
				return type.getActualTypeArguments()[0];
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int hashCode() {
		int result;
		result = resultClass.hashCode();
		result = 31 * result + propertyAccessor.hashCode();
		return result;
	}
}
