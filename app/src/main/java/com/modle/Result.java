package com.modle;

/**
 * Created by user on 2016/5/9.
 */
public class Result {
//    <RequestId>long</RequestId>
//    <AudioSegmentId>long</AudioSegmentId>
//    <Decision>NotSet or Mismatch or Inconclusive or Match or Failure or Unreliable</Decision>
//    <DecisionReason>NotSet or InternalError or AudioOK or NotEnoughAudio or InvalidAudio or AudioTooShort or AudioTooSoft or AudioTooLoud or AudioTooNoisy or MultiSpeakersDetected or SyntheticSpeechDetected or PlaybackIndication or ToneDetected or WrongPassphrase or FraudsterIndication or VoiceprintMatch or VoiceprintMismatch or VoiceprintInconclusive or UnreliableDecisionThresholds</DecisionReason>
//    <AdditionalInfo>string</AdditionalInfo>
//    <SpeakerId>string</SpeakerId>
//    <WatchListSuspect>string</WatchListSuspect>

    protected long requestId;
    protected long audioSegmentId;
    protected String decision;
    protected String decisionReason;
    protected String additionalInfo;
    protected String speakerId;
    protected String watchListSuspect;

    public long getRequestId() {
        return requestId;
    }

    public long getAudioSegmentId() {
        return audioSegmentId;
    }

    public String getDecision() {
        return decision;
    }

    public String getDecisionReason() {
        return decisionReason;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getSpeakerId() {
        return speakerId;
    }

    public String getWatchListSuspect() {
        return watchListSuspect;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public void setAudioSegmentId(long audioSegmentId) {
        this.audioSegmentId = audioSegmentId;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public void setDecisionReason(String decisionReason) {
        this.decisionReason = decisionReason;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void setSpeakerId(String speakerId) {
        this.speakerId = speakerId;
    }

    public void setWatchListSuspect(String watchListSuspect) {
        this.watchListSuspect = watchListSuspect;
    }
}
