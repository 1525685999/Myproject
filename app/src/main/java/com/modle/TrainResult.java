package com.modle;

/**
 * Created by user on 2016/5/9.
 */
public class TrainResult {
//    <TrainResult>
//    <RequestId>long</RequestId>
//    <TrainStatus>NotSet or Succeeded or Failed</TrainStatus>
//    <TrainReason>NotSet or InternalError or AudioOK or NotEnoughAudio or Inconsistency or NotReadyToTrain</TrainReason>
//    <AdditionalInfo>string</AdditionalInfo>
//    </TrainResult>
    protected long requestId;
    protected String trainStatus;
    protected String trainReason;
    protected String additionalInfo;

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public void setTrainStatus(String trainStatus) {
        this.trainStatus = trainStatus;
    }

    public void setTrainReason(String trainReason) {
        this.trainReason = trainReason;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public long getRequestId() {
        return requestId;
    }

    public String getTrainStatus() {
        return trainStatus;
    }

    public String getTrainReason() {
        return trainReason;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}
