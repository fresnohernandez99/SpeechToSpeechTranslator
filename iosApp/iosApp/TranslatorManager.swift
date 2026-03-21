//
//  Untitled.swift
//  iosApp
//
//  Created by eddy on 3/21/26.
//

import SharedUI
import MLKitTranslate

class TranslatorManager: TranslatorManagerIos {
    
    func checkSpecificModel(languageCode: String) async throws -> KotlinBoolean {
        let model = TranslateRemoteModel.translateRemoteModel(
            language: TranslateLanguage(rawValue: languageCode)
        )
        let isDownloaded = ModelManager.modelManager().isModelDownloaded(model)
        return KotlinBoolean(bool: isDownloaded)
    }
    
    func deleteSpecificModel(languageCode: String) async throws -> KotlinBoolean {
        return try await withCheckedThrowingContinuation { continuation in
            let model = TranslateRemoteModel.translateRemoteModel(
                language: TranslateLanguage(rawValue: languageCode)
            )
            ModelManager.modelManager().deleteDownloadedModel(model) { error in
                if let error = error {
                    continuation.resume(throwing: error)
                } else {
                    continuation.resume(returning: KotlinBoolean(bool: true))
                }
            }
        }
    }
    
    func downloadSpecificModel(languageCode: String, onStatusChange: @escaping (DownloadStatus) -> Void) {
        let model = TranslateRemoteModel.translateRemoteModel(
            language: TranslateLanguage(rawValue: languageCode)
        )
        let conditions = ModelDownloadConditions(
            allowsCellularAccess: false,
            allowsBackgroundDownloading: true
        )
        
        onStatusChange(DownloadStatus.Downloading())
        
        _ = ModelManager.modelManager().download(model, conditions: conditions)
        
        NotificationCenter.default.addObserver(
            forName: .mlkitModelDownloadDidSucceed,
            object: nil,
            queue: nil
        ) { notification in
            guard let userInfo = notification.userInfo,
                  let downloadedModel = userInfo[ModelDownloadUserInfoKey.remoteModel.rawValue] as? TranslateRemoteModel,
                  downloadedModel == model else { return }
            onStatusChange(DownloadStatus.Success())
        }
        
        NotificationCenter.default.addObserver(
            forName: .mlkitModelDownloadDidFail,
            object: nil,
            queue: nil
        ) { notification in
            guard let userInfo = notification.userInfo,
                  let error = userInfo[ModelDownloadUserInfoKey.error.rawValue] as? Error else { return }
            onStatusChange(DownloadStatus.Error(message: error.localizedDescription))
        }
    }
    
    func getDownloadedModels() async throws -> [String] {
        let models = ModelManager.modelManager().downloadedTranslateModels
        return models.map { $0.language.rawValue }
    }
    
    func translateUsingModel(
        text: String,
        sourceLanguage: String,
        targetLanguage: String,
        completionHandler: @escaping @Sendable (String?, (any Error)?) -> Void
    ) {
        let options = TranslatorOptions(
            sourceLanguage: TranslateLanguage(rawValue: sourceLanguage),
            targetLanguage: TranslateLanguage(rawValue: targetLanguage)
        )
        let translator = Translator.translator(options: options)
        
        translator.translate(text) { result, error in
            if let error = error {
                completionHandler(nil, error)
            } else {
                completionHandler(result, nil)
            }
        }
    }
}

