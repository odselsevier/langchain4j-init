import Foundation
import AppKit
import CoreGraphics
import CoreText
import ImageIO
import UniformTypeIdentifiers

struct LogLine {
    let at: Double
    let text: String
    let color: NSColor
}

let width = 3840
let height = 2160
let fps = 30
let duration = 15.0
let totalFrames = Int(duration * Double(fps))

let outDir = URL(fileURLWithPath: FileManager.default.currentDirectoryPath)
    .appendingPathComponent("/tmp/terminal_frames_4k")
try? FileManager.default.removeItem(at: outDir)
try FileManager.default.createDirectory(at: outDir, withIntermediateDirectories: true)

let font = NSFont(name: "Menlo", size: 35) ?? NSFont.monospacedSystemFont(ofSize: 35, weight: .regular)
let promptFont = NSFont(name: "Menlo-Bold", size: 35) ?? NSFont.monospacedSystemFont(ofSize: 35, weight: .semibold)

let neonGreen = NSColor(calibratedRed: 0.43, green: 1.0, blue: 0.55, alpha: 1.0)
let neonBlue = NSColor(calibratedRed: 0.33, green: 0.73, blue: 1.0, alpha: 1.0)
let dimGreen = NSColor(calibratedRed: 0.30, green: 0.82, blue: 0.44, alpha: 1.0)
let softWhite = NSColor(calibratedRed: 0.90, green: 0.97, blue: 0.93, alpha: 1.0)

let prompt = "dykyio@dev ~/Documents/GitHub/langchain4j-init % "
let command = "java -jar target/langchain4j-init-1.0.0-SNAPSHOT.jar faq"

let logs: [LogLine] = [
    .init(at: 3.10, text: "14:22:10.104 [main] INFO  com.showcase.Main - Launching demo: faq", color: dimGreen),
    .init(at: 3.45, text: "14:22:10.119 [main] INFO  com.showcase.features.ToolCallingDemo - === Website FAQ Demo (RAG-style) ===", color: neonBlue),
    .init(at: 4.05, text: "14:22:10.361 [main] DEBUG dev.langchain4j.service.AiServices - Building AI service: AssistantWithTools", color: dimGreen),
    .init(at: 4.70, text: "14:22:10.624 [main] DEBUG dev.langchain4j.service.AiServices - Registered tools: WebsiteFaqTools.searchFaq, InventoryTools.checkInventory", color: dimGreen),
    .init(at: 5.55, text: "14:22:11.052 [main] INFO  com.showcase.features.ToolCallingDemo - User -> What events are next and stock for PRODUCT-99?", color: dimGreen),
    .init(at: 6.40, text: "14:22:11.468 [main] INFO  dev.langchain4j.model.ollama.OllamaChatModel - Tool execution requested: searchFaq(userQuestion)", color: dimGreen),
    .init(at: 7.30, text: "14:22:11.924 [main] INFO  dev.langchain4j.model.ollama.OllamaChatModel - Tool execution requested: checkInventory(sku=PRODUCT-99)", color: dimGreen),
    .init(at: 8.25, text: "14:22:12.353 [main] INFO  com.showcase.ai.InventoryTools - checkInventory(PRODUCT-99) -> SKU-PRODUCT-99: 42 products in stock", color: dimGreen),
    .init(at: 9.60, text: "14:22:12.857 [main] INFO  com.showcase.features.ToolCallingDemo - Assistant -> Events are in the FAQ. SKU-PRODUCT-99 has 42 products in stock.", color: softWhite)
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
    let g1 = color.withAlphaComponent(0.20)
    let g2 = color.withAlphaComponent(0.10)
    drawLine(context, text: text, x: x - 1.0, y: y - 1.0, font: font, color: g1)
    drawLine(context, text: text, x: x + 1.0, y: y + 1.0, font: font, color: g1)
    drawLine(context, text: text, x: x, y: y, font: font, color: g2)
}

func ease(_ t: Double) -> Double {
    1.0 - pow(1.0 - t, 2.0)
}

for frame in 0..<totalFrames {
    let t = Double(frame) / Double(fps)
    let bytesPerRow = width * 4
    var data = Data(count: bytesPerRow * height)

    let ok = data.withUnsafeMutableBytes { rawBuffer -> Bool in
        guard let base = rawBuffer.baseAddress else { return false }
        guard let context = CGContext(
            data: base,
            width: width,
            height: height,
            bitsPerComponent: 8,
            bytesPerRow: bytesPerRow,
            space: CGColorSpaceCreateDeviceRGB(),
            bitmapInfo: CGImageAlphaInfo.premultipliedLast.rawValue
        ) else {
            return false
        }

        context.translateBy(x: 0, y: CGFloat(height))
        context.scaleBy(x: 1, y: -1)
        context.textMatrix = CGAffineTransform(scaleX: 1, y: -1)

        context.setFillColor(NSColor(calibratedWhite: 0.01, alpha: 1.0).cgColor)
        context.fill(CGRect(x: 0, y: 0, width: width, height: height))

        let s = CGFloat(1.0)
        context.saveGState()
        context.translateBy(x: CGFloat(width) * 0.5, y: CGFloat(height) * 0.5)
        context.scaleBy(x: s, y: s)
        context.translateBy(x: -CGFloat(width) * 0.5, y: -CGFloat(height) * 0.5)

        context.setFillColor(NSColor(calibratedRed: 0.01, green: 0.03, blue: 0.03, alpha: 1.0).cgColor)
        context.fill(CGRect(x: 0, y: 0, width: width, height: height))

        context.setFillColor(NSColor(calibratedWhite: 0.03, alpha: 1.0).cgColor)
        context.fill(CGRect(x: 0, y: 1520, width: width, height: height - 1520))

        let monitorRect = CGRect(x: 370, y: 220, width: 3100, height: 1700)
        context.setFillColor(NSColor(calibratedWhite: 0.04, alpha: 1.0).cgColor)
        context.addPath(CGPath(roundedRect: monitorRect, cornerWidth: 26, cornerHeight: 26, transform: nil))
        context.fillPath()

        context.setStrokeColor(NSColor(calibratedRed: 0.2, green: 0.98, blue: 0.56, alpha: 0.16).cgColor)
        context.setLineWidth(2)
        context.addPath(CGPath(roundedRect: monitorRect.insetBy(dx: 3, dy: 3), cornerWidth: 24, cornerHeight: 24, transform: nil))
        context.strokePath()

        let screenRect = CGRect(x: 500, y: 350, width: 2840, height: 1370)
        context.setFillColor(NSColor(calibratedRed: 0.0, green: 0.01, blue: 0.005, alpha: 1.0).cgColor)
        context.addPath(CGPath(roundedRect: screenRect, cornerWidth: 16, cornerHeight: 16, transform: nil))
        context.fillPath()

        let tx = screenRect.minX + 66
        let ty = screenRect.minY + 110
        let lineHeight: CGFloat = 46

        let typeProgress = max(0.0, min(1.0, t / 1.0))
        let typedCount = Int(Double(command.count) * typeProgress)
        let typed = String(command.prefix(typedCount))

        drawGlowLine(context, text: prompt, x: tx, y: ty, font: promptFont, color: dimGreen)
        drawLine(context, text: prompt, x: tx, y: ty, font: promptFont, color: dimGreen)

        let promptW = CGFloat((prompt as NSString).size(withAttributes: [.font: promptFont]).width)
        let cmdText = t < 1.0 ? typed : command
        drawGlowLine(context, text: cmdText, x: tx + promptW, y: ty, font: promptFont, color: neonGreen)
        drawLine(context, text: cmdText, x: tx + promptW, y: ty, font: promptFont, color: neonGreen)

        if t < 1.0 {
            let cursorVisible = (t < 0.42) || (t > 0.62 && t < 0.82)
            if cursorVisible {
                let cmdW = CGFloat((cmdText as NSString).size(withAttributes: [.font: promptFont]).width)
                context.setFillColor(neonGreen.withAlphaComponent(0.95).cgColor)
                context.fill(CGRect(x: tx + promptW + cmdW + 4, y: ty + 6, width: 16, height: 40))
            }
        }

        var visibleLogs = logs.filter { t >= $0.at }
        let maxLines = Int((screenRect.height - 220) / lineHeight)
        if visibleLogs.count > maxLines {
            visibleLogs = Array(visibleLogs.suffix(maxLines))
        }

        for (idx, line) in visibleLogs.enumerated() {
            let y = ty + lineHeight * CGFloat(idx + 1)
            drawGlowLine(context, text: line.text, x: tx, y: y, font: font, color: line.color)
            drawLine(context, text: line.text, x: tx, y: y, font: font, color: line.color)
        }

        context.setStrokeColor(NSColor(calibratedWhite: 0.0, alpha: 0.10).cgColor)
        context.setLineWidth(1)
        var scanY = Int(screenRect.minY)
        while scanY < Int(screenRect.maxY) {
            context.move(to: CGPoint(x: screenRect.minX, y: CGFloat(scanY)))
            context.addLine(to: CGPoint(x: screenRect.maxX, y: CGFloat(scanY)))
            scanY += 3
        }
        context.strokePath()

        context.restoreGState()

        return true
    }

    guard ok else { throw NSError(domain: "render", code: 1) }

    let url = outDir.appendingPathComponent(String(format: "frame_%04d.png", frame))

    let provider = CGDataProvider(data: data as CFData)!
    let cg = CGImage(
        width: width,
        height: height,
        bitsPerComponent: 8,
        bitsPerPixel: 32,
        bytesPerRow: width * 4,
        space: CGColorSpaceCreateDeviceRGB(),
        bitmapInfo: CGBitmapInfo(rawValue: CGImageAlphaInfo.premultipliedLast.rawValue),
        provider: provider,
        decode: nil,
        shouldInterpolate: true,
        intent: .defaultIntent
    )!

    guard let dest = CGImageDestinationCreateWithURL(url as CFURL, UTType.png.identifier as CFString, 1, nil) else {
        throw NSError(domain: "render", code: 2)
    }
    CGImageDestinationAddImage(dest, cg, nil)
    guard CGImageDestinationFinalize(dest) else {
        throw NSError(domain: "render", code: 3)
    }

    if frame % 15 == 0 {
        print("Rendered \(frame + 1)/\(totalFrames)")
    }
}

print("Frames written to \(outDir.path)")
