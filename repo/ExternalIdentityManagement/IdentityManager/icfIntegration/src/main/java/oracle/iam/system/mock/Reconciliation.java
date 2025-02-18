package oracle.iam.system.mock;

import java.util.concurrent.ExecutorService;

import oracle.iam.connectors.icfcommon.IResourceConfig;
import oracle.iam.connectors.icfcommon.ITResource;
import oracle.iam.connectors.icfcommon.Lookup;
import oracle.iam.connectors.icfcommon.concurrent.ThreadPoolConfig;
import oracle.iam.connectors.icfcommon.recon.ReconEvent;
import oracle.iam.connectors.icfcommon.service.ReconciliationService;

import org.identityconnectors.framework.common.objects.ConnectorObject;

public class Reconciliation implements ReconciliationService {

  private class BatchReconciliationService implements ReconciliationService.BatchReconciliationService {
    @Override
    public void addEvent(final ReconEvent event) {
      // intentionally left blank
    }
    @Override
    public void finish() {
      // intentionally left blank
    }
  }

  private class ConcurrentBatchReconciliationService implements ReconciliationService.ConcurrentBatchReconciliationService {
    @Override
    public void addToBatchForProcessing(final ConnectorObject object) {
      // intentionally left blank
    }
    @Override
    public void finish() {
      // intentionally left blank
    }
  }

  private class DeletionDetectionService implements ReconciliationService.DeletionDetectionService {
    @Override
    public void addObjectOnResource(ReconEvent reconEvent) {
      // intentionally left blank
    }
    @Override
    public void deleteUnmatched() {
      // intentionally left blank
    }
  }

  public Reconciliation() {
    super();
  }

  @Override
  public String getDefaultDateFormat() {
    return "d MMM yyyy HH:mm:ss Z";
  }

  @Override
  public void setTaskParameter(String taskName, String paramName, String paramValue) {
  }

  @Override
  public void runJob(String jobName) {
  }

  @Override
  public ReconciliationService.DeletionDetectionService createDeleteDetectionService(ITResource itResource, String resourceObjectName) {
    return new DeletionDetectionService();
  }

  @Override
  public ReconciliationService.BatchReconciliationService createBatchReconciliationService(String resourceObjectName, int batchSize, boolean ignoreEventDisabled) {
    return new BatchReconciliationService();
  }

  @Override
  public ReconciliationService.BatchReconciliationService createBatchReconciliationService(String resourceObjectName, int batchSize, boolean ignoreEventDisabled, ThreadPoolConfig pool, int p5) {
    return new BatchReconciliationService();
  }

  @Override
  public ReconciliationService.ConcurrentBatchReconciliationService createConcurrentBatchReconciliationService(String resourceObjectName, ITResource itResource, Lookup p3, String p4, ExecutorService p5, IResourceConfig p6) {
    return new ConcurrentBatchReconciliationService();
  }


  @Override
  public Boolean processEvent(String resourceObjectName, ReconEvent reconEvent) {
    return true;
  }

  @Override
  public Boolean processDeleteEvent(String resourceObjectName, ReconEvent reconEvent) {
    return true;
  }
}
