package oracle.iam.junit.scim.dto;

public class Entity {
  Long id;
  
  public Entity(final Long id) {
    // ensure inheritance
    super();

    this.id = id;
  }
}