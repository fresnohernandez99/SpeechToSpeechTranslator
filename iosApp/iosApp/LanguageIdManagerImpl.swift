//
//  Untitled.swift
//  iosApp
//
//  Created by eddy on 3/21/26.
//

import SharedUI
import MLKitLanguageID


class LanguageIdManagerImpl: LanguageIdManagerIos {

    let options = LanguageIdentificationOptions(confidenceThreshold: 0.01)

    let languageId = LanguageIdentification.languageIdentification(options: options)

    func getLanguage(
        text: String,
        completionHandler: @escaping @Sendable (String?, (any Error)?) -> Void
    ) {
        languageId.identifyLanguage(for: text) { (languageCode, error) in
          if let error = error {
            completionHandler(nil, error)
          }
          if let languageCode = languageCode, languageCode != "und" {
            completionHandler(languageCode, nil)
            print("Identified Language: \(languageCode)")
          } else {
            completionHandler(nil, nil)
          }
        }
        // TODO TEST THIS PORTION
    }
}

