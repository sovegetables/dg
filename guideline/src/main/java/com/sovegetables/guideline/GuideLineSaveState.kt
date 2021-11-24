package com.sovegetables.guideline

import com.sovegetables.kv_cache.SpHelper

data class GuideLineSaveState(var step: Int = NEW_STEP_FIRST_ME_TAB, var showed: Int = 0) {

    companion object{
        /**
         * 首次"我的" - Flag
         */
        const val NEW_STEP_FIRST_ME_TAB = 10

        /**
         * 首次"实名认证" - Flag
         */
        const val NEW_STEP_FIRST_AUTH = 11

        /**
         * 首次"申请班组" - Flag
         */
        const val NEW_STEP_APPLY_GROUP = 12

        /**
         * "申请班组"后扫二维码 - Flag
         */
        const val NEW_STEP_QR_CODE = 13

        const val NEW_STEP_TASK = 14

        const val NEW_STEP_LOOK_TASK = 15

        const val NEW_STEP_NOT = 16


        private const val KEY_GUIDE_LINE = "KEY.GUIDE.LINE"

        fun save(state: GuideLineSaveState){
            SpHelper.saveData(KEY_GUIDE_LINE, state)
        }

        fun getGuideLineSaveState(): GuideLineSaveState {
            return SpHelper.getData(KEY_GUIDE_LINE, GuideLineSaveState::class.java)
                ?: return GuideLineSaveState(
                    NEW_STEP_FIRST_ME_TAB,
                    0
                )
        }

        fun isNeedShowFirstMeTab2(): Boolean{
            return getGuideLineSaveState().step <= NEW_STEP_FIRST_AUTH && getGuideLineSaveState().showed == 0
        }

        fun isNeedShowFirstAuth2(): Boolean{
            return getGuideLineSaveState().step == NEW_STEP_FIRST_AUTH && getGuideLineSaveState().showed == 0
        }

        fun isNeedShowApplyGroup(): Boolean{
            return getGuideLineSaveState().step == NEW_STEP_APPLY_GROUP && getGuideLineSaveState().showed == 0
        }

        fun isNeedShowQRCode2(): Boolean{
            return getGuideLineSaveState().step == NEW_STEP_QR_CODE && getGuideLineSaveState().showed == 0
        }

        fun isNeedShowTask2(): Boolean{
            return getGuideLineSaveState().step in NEW_STEP_TASK..NEW_STEP_LOOK_TASK && getGuideLineSaveState().showed == 0
        }

        fun isNeedShowLookTask2(): Boolean{
            return getGuideLineSaveState().step == NEW_STEP_LOOK_TASK && getGuideLineSaveState().showed == 0
        }

        fun isGuideLineNotDone2(): Boolean {
            return getGuideLineSaveState().step < NEW_STEP_NOT
        }
    }
}