package com.modle;

/**
 * Created by user on 2016/5/9.
 */
public class EnrollResult {
//    <EnrollResult>
//    <EnrollStatus>MoreAudioRequired or ReadyForTraining or Full</EnrollStatus>
//    <RequestId>long</RequestId>
//    <AudioSegmentId>long</AudioSegmentId>
//    <IsSegmentValid>boolean</IsSegmentValid>
//    <Reason>NotSet or InternalError or AudioOK or NotEnoughAudio or InvalidAudio or AudioTooShort or AudioTooSoft or AudioTooLoud or AudioTooNoisy or MultiSpeakersDetected or SyntheticSpeechDetected or PlaybackIndication or WrongPassphrase or FraudsterIndication</Reason>
//    <AdditionalInfo>string</AdditionalInfo>
//    </EnrollResult>

    protected String enrollStatus;
    protected long requestId;
    protected long audioSegmentId;
    protected boolean isSegmentValid;
    protected String reason;
    protected String additionalInfo;


    public long getAudioSegmentId() {
        return audioSegmentId;
    }

    public String getEnrollStatus() {
        return enrollStatus;
    }

    public long getRequestId() {
        return requestId;
    }

    public boolean isSegmentValid() {
        return isSegmentValid;
    }

    public String getReason() {
        return reason;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setEnrollStatus(String enrollStatus) {
        this.enrollStatus = enrollStatus;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public void setAudioSegmentId(long audioSegmentId) {
        this.audioSegmentId = audioSegmentId;
    }

    public void setSegmentValid(boolean segmentValid) {
        isSegmentValid = segmentValid;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }


}
