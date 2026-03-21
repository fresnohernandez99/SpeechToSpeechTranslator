package com.fresnohernandez99.stpt.service

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.fresnohernandez99.stpt.R
import com.fresnohernandez99.stpt.extensions.launchMainActivityFromTile
import com.fresnohernandez99.stpt.extensions.startRecordingService

class RecordingTileService : TileService() {

    override fun onClick() {
        if (qsTile.state == Tile.STATE_INACTIVE) {
            this.launchMainActivityFromTile()
            return
        }
        stopRecording()
    }

    private fun stopRecording() {
        this.startRecordingService(recordingAction = AudioRecordingService.ACTION_STOP_FROM_TILE)
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.icon = Icon.createWithResource(this, R.drawable.ic_outline_edit_note)
        qsTile.updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()
        if (AudioRecordingService.isRunning) {
            qsTile.state = Tile.STATE_ACTIVE
            qsTile.icon = Icon.createWithResource(this, android.R.drawable.ic_media_pause)
        } else {
            qsTile.state = Tile.STATE_INACTIVE
            qsTile.icon = Icon.createWithResource(this, R.drawable.ic_outline_edit_note)
        }
        qsTile.updateTile()
    }
}
