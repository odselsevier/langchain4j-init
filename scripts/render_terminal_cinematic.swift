import Foundation
import AVFoundation
import CoreGraphics
import CoreText
import AppKit

struct LogLine {
    let at: Double
    let text: String
    let color: NSColor
}

let width = 3840
let height = 2160
let fps: Int32 = 30
let duration: Double = 5.0
let totalFrames = Int(duration * Double(fps))

let formatter = DateFormatter()
formatter.dateFormat = "yyyyMMdd-HHmmss"
let ts = formatter.string(from: Date())
let outputURL = URL(fileURLWithPath: FileManager.default.currentDirectoryPath)
    .appendingPathComponent("media/\(ts)-langchain4j-init-terminal-cinematic.mov")

try? FileManager.default.removeItem(at: outputURL)

let writer = try AVAssetWriter(outputURL: outputURL, fileType: .mov)
let settings: [String: Any] = [
    AVVideoCodecKey: AVVideoCodecType.jpeg,
    AVVideoWidthKey: width,
    AVVideoHeightKey: height
]

let input = AVAssetWriterInput(mediaType: .video, outputSettings: settings)
input.expectsMediaDataInRealTime = false

let sourceAttrs: [String: Any] = [
    kCVPixelBufferPixelFormatTypeKey as String: Int(kCVPixelFormatType_32BGRA),
    kCVPixelBufferWidthKey as String: width,
    kCVPixelBufferHeightKey as String: height,
    kCVPixelBufferCGImageCompatibilityKey as String: true,
    kCVPixelBufferCGBitmapContextCompatibilityKey as String: true,
    kCVPixelBufferIOSurfacePropertiesKey as String: [:]
]

let adaptor = AVAssetWriterInputPixelBufferAdaptor(assetWriterInput: input, sourcePixelBufferAttributes: sourceAttrs)
precondition(writer.canAdd(input))
writer.add(input)

let font = NSFont(name: "Menlo", size: 40) ?? NSFont.monospacedSystemFont(ofSize: 40, weight: .regular)
let promptFont = NSFont(name: "Menlo-Bold", size: 40) ?? NSFont.monospacedSystemFont(ofSize: 40, weight: .semibold)

let neonGreen = NSColor(calibratedRed: 0.43, green: 1.0, blue: 0.55, alpha: 1.0)
let neonBlue = NSColor(calibratedRed: 0.33, green: 0.73, blue: 1.0, alpha: 1.0)
let dimGreen = NSColor(calibratedRed: 0.30, green: 0.82, blue: 0.44, alpha: 1.0)
let softWhite = NSColor(calibratedRed: 0.86, green: 0.94, blue: 0.90, alpha: 1.0)

let prompt = "dykyio@dev ~/Documents/GitHub/langchain4j-init % "
let command = "java -jar target/langchain4j-init-1.0.0-SNAPSHOT.jar tools"

let logs: [LogLine] = [
    .init(at: 1.05, text: "14:22:10.104 [main] INFO  com.showcase.Main - Launching demo: tools", color: dimGreen),
    .init(at: 1.15, text: "14:22:10.119 [main] INFO  com.showcase.features.ToolCallingDemo - === Tool Calling Demo ===", color: neonBlue),
    .init(at: 1.36, text: "14:22:10.361 [main] DEBUG dev.langchain4j.service.AiServices - Building AI service: AssistantWithTools", color: dimGreen),
    .init(at: 1.62, text: "14:22:10.624 [main] DEBUG dev.langchain4j.service.AiServices - Registered tools: InventoryTools.checkInventory, InventoryTools.restockOrder", color: dimGreen),
    .init(at: 2.05, text: "14:22:11.052 [main] INFO  dev.langchain4j.model.ollama.OllamaChatModel - Sending chat request to http://localhost:11434", color: dimGreen),
    .init(at: 2.46, text: "14:22:11.468 [main] INFO  dev.langchain4j.model.ollama.OllamaChatModel - Tool execution requested: checkInventory(sku=PRODUCT-99)", color: dimGreen),
    .init(at: 3.05, text: "14:22:12.053 [main] INFO  com.showcase.ai.InventoryTools - checkInventory(PRODUCT-99) -> SKU-PRODUCT-99: 42 products in stock", color: dimGreen),
    .init(at: 3.75, text: "14:22:12.757 [main] INFO  com.showcase.features.ToolCallingDemo - Assistant → We currently have SKU-PRODUCT-99: 42 products in stock.", color: softWhite)
]

func drawLine(_ context: CGContext, text: String, x: CGFloat, y: CGFloat, font: NSFont, color: NSColor) {
    let attrs: [NSAttributedString.Key: Any] = [
        .font: font,
        .foregroundColor: color
    ]
    let line = CTLineCreateWithAttributedString(NSAttributedString(string: text, attributes: attrs))

    context.textPosition = CGPoint(x: x, y: y)
    CTLineDraw(line, context)
}

func drawGlowLine(_ context: CGContext, text: String, x: CGFloat, y: CGFloat, font: NSFont, color: NSColor) {
    let g1 = color.withAlphaComponent(0.22)
    let g2 = color.withAlphaComponent(0.10)
    drawLine(context, text: text, x: x - 1.2, y: y - 1.2, font: font, color: g1)
    drawLine(context, text: text, x: x + 1.2, y: y + 1.2, font: font, color: g1)
    drawLine(context, text: text, x: x, y: y, font: font, color: g2)
}

func ease(_ t: Double) -> Double {
    // Smooth camera push
    return 1.0 - pow(1.0 - t, 2.0)
}

writer.startWriting()
writer.startSession(atSourceTime: .zero)

let queue = DispatchQueue(label: "render.queue")

input.requestMediaDataWhenReady(on: queue) {
    var frame = 0

    while frame < totalFrames {
        if !input.isReadyForMoreMediaData {
            usleep(1000)
            continue
        }

        let t = Double(frame) / Double(fps)

        var pixelBufferOpt: CVPixelBuffer?
        let status = CVPixelBufferCreate(
            kCFAllocatorDefault,
            width,
            height,
            kCVPixelFormatType_32BGRA,
            nil,
            &pixelBufferOpt
        )
        guard status == kCVReturnSuccess, let pixelBuffer = pixelBufferOpt else {
            fatalError("Could not create pixel buffer")
        }

        CVPixelBufferLockBaseAddress(pixelBuffer, [])
        let baseAddress = CVPixelBufferGetBaseAddress(pixelBuffer)!
        let bytesPerRow = CVPixelBufferGetBytesPerRow(pixelBuffer)
        let colorSpace = CGColorSpaceCreateDeviceRGB()

        guard let context = CGContext(
            data: baseAddress,
            width: width,
            height: height,
            bitsPerComponent: 8,
            bytesPerRow: bytesPerRow,
            space: colorSpace,
            bitmapInfo: CGImageAlphaInfo.premultipliedFirst.rawValue
        ) else {
            fatalError("Could not create graphics context")
        }

        // Convert to top-left coordinate space.
        context.translateBy(x: 0, y: CGFloat(height))
        context.scaleBy(x: 1, y: -1)

        // Base background.
        context.setFillColor(NSColor(calibratedWhite: 0.01, alpha: 1.0).cgColor)
        context.fill(CGRect(x: 0, y: 0, width: width, height: height))

        // Camera dolly-in.
        let s = CGFloat(1.0 + 0.055 * ease(min(max(t / duration, 0.0), 1.0)))
        context.saveGState()
        context.translateBy(x: CGFloat(width) * 0.5, y: CGFloat(height) * 0.5)
        context.scaleBy(x: s, y: s)
        context.translateBy(x: -CGFloat(width) * 0.5, y: -CGFloat(height) * 0.5)

        // Atmospheric background bloom.
        if let bgGrad = CGGradient(
            colorsSpace: colorSpace,
            colors: [
                NSColor(calibratedRed: 0.01, green: 0.01, blue: 0.015, alpha: 1.0).cgColor,
                NSColor(calibratedRed: 0.01, green: 0.05, blue: 0.04, alpha: 1.0).cgColor,
                NSColor(calibratedRed: 0.005, green: 0.005, blue: 0.008, alpha: 1.0).cgColor
            ] as CFArray,
            locations: [0.0, 0.52, 1.0]
        ) {
            context.drawLinearGradient(bgGrad, start: CGPoint(x: 0, y: 150), end: CGPoint(x: 0, y: CGFloat(height)), options: [])
        }

        // Desk matte.
        let deskY = CGFloat(1520)
        context.setFillColor(NSColor(calibratedWhite: 0.03, alpha: 1.0).cgColor)
        context.fill(CGRect(x: 0, y: deskY, width: CGFloat(width), height: CGFloat(height) - deskY))

        // Keyboard edge hints.
        for i in 0..<26 {
            let x = CGFloat(420 + i * 115)
            let alpha = 0.03 + CGFloat(i % 5) * 0.004
            context.setFillColor(NSColor(calibratedRed: 0.05, green: 0.16, blue: 0.14, alpha: alpha).cgColor)
            context.fill(CGRect(x: x, y: 1720 + CGFloat(i % 3) * 8, width: 78, height: 10))
        }

        // Monitor frame + subtle chromatic edge.
        let monitorRect = CGRect(x: 370, y: 220, width: 3100, height: 1700)
        context.setFillColor(NSColor(calibratedWhite: 0.04, alpha: 1.0).cgColor)
        context.fillRoundedRect(in: monitorRect, cornerWidth: 26, cornerHeight: 26)

        context.setStrokeColor(NSColor(calibratedRed: 0.2, green: 0.98, blue: 0.56, alpha: 0.18).cgColor)
        context.setLineWidth(2)
        context.strokeRoundedRect(in: monitorRect.insetBy(dx: 3, dy: 3), cornerWidth: 24, cornerHeight: 24)

        context.setStrokeColor(NSColor(calibratedRed: 0.25, green: 0.55, blue: 1.0, alpha: 0.16).cgColor)
        context.setLineWidth(2)
        context.strokeRoundedRect(in: monitorRect.insetBy(dx: 1, dy: 1).offsetBy(dx: 1, dy: 0), cornerWidth: 24, cornerHeight: 24)

        // Terminal viewport.
        let screenRect = CGRect(x: 500, y: 350, width: 2840, height: 1370)
        context.setFillColor(NSColor(calibratedRed: 0.0, green: 0.01, blue: 0.005, alpha: 1.0).cgColor)
        context.fillRoundedRect(in: screenRect, cornerWidth: 16, cornerHeight: 16)

        // Reflection smear.
        context.setFillColor(NSColor(calibratedRed: 0.45, green: 0.9, blue: 1.0, alpha: 0.05).cgColor)
        context.fill(CGRect(x: screenRect.minX + 120, y: screenRect.minY + 46, width: 420, height: 8))

        // Terminal text region.
        let tx = screenRect.minX + 66
        let ty = screenRect.minY + 110
        let lineHeight: CGFloat = 52

        let typeProgress = max(0.0, min(1.0, t / 1.0))
        let typedCount = Int(Double(command.count) * typeProgress)
        let typed = String(command.prefix(typedCount))

        // Prompt line.
        drawGlowLine(context, text: prompt, x: tx, y: ty, font: promptFont, color: dimGreen)
        drawLine(context, text: prompt, x: tx, y: ty, font: promptFont, color: dimGreen)

        // Typed command.
        let promptW = CGFloat((prompt as NSString).size(withAttributes: [.font: promptFont]).width)
        let cmdText = t < 1.0 ? typed : command
        drawGlowLine(context, text: cmdText, x: tx + promptW, y: ty, font: promptFont, color: neonGreen)
        drawLine(context, text: cmdText, x: tx + promptW, y: ty, font: promptFont, color: neonGreen)

        // Cursor blinks once in input window.
        if t < 1.0 {
            let cursorVisible = (t < 0.42) || (t > 0.62 && t < 0.82)
            if cursorVisible {
                let cmdW = CGFloat((cmdText as NSString).size(withAttributes: [.font: promptFont]).width)
                context.setFillColor(neonGreen.withAlphaComponent(0.95).cgColor)
                context.fill(CGRect(x: tx + promptW + cmdW + 4, y: ty + 6, width: 16, height: 40))
            }
        }

        // Logs appear after Enter.
        var visibleLogs = logs.filter { t >= $0.at }

        // Add a subtle scroll if lines exceed viewport budget.
        let maxLines = Int((screenRect.height - 220) / lineHeight)
        if visibleLogs.count > maxLines {
            visibleLogs = Array(visibleLogs.suffix(maxLines))
        }

        for (idx, line) in visibleLogs.enumerated() {
            let y = ty + lineHeight * CGFloat(idx + 1)
            let isFinal = line.text.contains("Assistant →")
            let color = isFinal ? softWhite : line.color
            let fontToUse = isFinal ? NSFont(name: "Menlo-Bold", size: 40) ?? font : font

            drawGlowLine(context, text: line.text, x: tx, y: y, font: fontToUse, color: color)
            drawLine(context, text: line.text, x: tx, y: y, font: fontToUse, color: color)
        }

        // Subtle scanlines over terminal screen.
        context.setStrokeColor(NSColor(calibratedWhite: 0.0, alpha: 0.11).cgColor)
        context.setLineWidth(1)
        var y = Int(screenRect.minY)
        while y < Int(screenRect.maxY) {
            context.move(to: CGPoint(x: screenRect.minX, y: CGFloat(y)))
            context.addLine(to: CGPoint(x: screenRect.maxX, y: CGFloat(y)))
            y += 3
        }
        context.strokePath()

        // Green-blue key light spill to desk.
        context.setFillColor(NSColor(calibratedRed: 0.18, green: 0.95, blue: 0.72, alpha: 0.06).cgColor)
        context.fillEllipse(in: CGRect(x: 620, y: 1460, width: 2600, height: 620))

        context.setFillColor(NSColor(calibratedRed: 0.18, green: 0.58, blue: 1.0, alpha: 0.045).cgColor)
        context.fillEllipse(in: CGRect(x: 720, y: 1500, width: 2400, height: 560))

        context.restoreGState()

        // Global vignette.
        if let vignette = CGGradient(
            colorsSpace: colorSpace,
            colors: [
                NSColor(calibratedWhite: 0.0, alpha: 0.0).cgColor,
                NSColor(calibratedWhite: 0.0, alpha: 0.0).cgColor,
                NSColor(calibratedWhite: 0.0, alpha: 0.45).cgColor
            ] as CFArray,
            locations: [0.0, 0.72, 1.0]
        ) {
            context.drawRadialGradient(vignette,
                                       startCenter: CGPoint(x: CGFloat(width) * 0.5, y: CGFloat(height) * 0.45),
                                       startRadius: CGFloat(width) * 0.14,
                                       endCenter: CGPoint(x: CGFloat(width) * 0.5, y: CGFloat(height) * 0.45),
                                       endRadius: CGFloat(width) * 0.85,
                                       options: [])
        }

        let time = CMTime(value: CMTimeValue(frame), timescale: fps)
        if !adaptor.append(pixelBuffer, withPresentationTime: time) {
            print("Append failed at frame \(frame). status=\(writer.status.rawValue) error=\(writer.error?.localizedDescription ?? "none")")
            input.markAsFinished()
            writer.cancelWriting()
            CFRunLoopStop(CFRunLoopGetMain())
            return
        }

        CVPixelBufferUnlockBaseAddress(pixelBuffer, [])

        if frame % 15 == 0 {
            print("Rendered frame \(frame + 1)/\(totalFrames)")
        }

        frame += 1
    }

    input.markAsFinished()
    writer.finishWriting {
        print("Done -> \(outputURL.path)")
        CFRunLoopStop(CFRunLoopGetMain())
    }
}

CFRunLoopRun()

private extension CGContext {
    func fillRoundedRect(in rect: CGRect, cornerWidth: CGFloat, cornerHeight: CGFloat) {
        let path = CGPath(roundedRect: rect, cornerWidth: cornerWidth, cornerHeight: cornerHeight, transform: nil)
        addPath(path)
        fillPath()
    }

    func strokeRoundedRect(in rect: CGRect, cornerWidth: CGFloat, cornerHeight: CGFloat) {
        let path = CGPath(roundedRect: rect, cornerWidth: cornerWidth, cornerHeight: cornerHeight, transform: nil)
        addPath(path)
        strokePath()
    }
}
