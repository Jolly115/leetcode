//
//  ContentView.swift
//  navigation
//
//  Created by Jolly Gupta on 11/24/25.
//

import SwiftUI

// MARK: - Main Content View with TabView
struct ContentView: View {
    var body: some View {
        TabView {
            NavigationStackDemo()
                .tabItem {
                    Label("Stack", systemImage: "square.stack.3d.up")
                }
            
            ProgrammaticNavigationDemo()
                .tabItem {
                    Label("Programmatic", systemImage: "arrow.triangle.branch")
                }
            
            ModalsDemo()
                .tabItem {
                    Label("Modals", systemImage: "rectangle.portrait.and.arrow.right")
                }
            
            PopoversDemo()
                .tabItem {
                    Label("Popovers", systemImage: "bubble.left.and.bubble.right")
                }
        }
    }
}

// MARK: - NavigationStack & NavigationLink Demo
struct NavigationStackDemo: View {
    var body: some View {
        NavigationStack {
            List {
                Section("Basic Navigation Links") {
                    NavigationLink("Simple Detail View") {
                        DetailView(title: "Simple Detail", color: .blue)
                    }
                    
                    NavigationLink {
                        DetailView(title: "Custom Navigation", color: .purple)
                    } label: {
                        HStack {
                            Image(systemName: "star.fill")
                                .foregroundStyle(.yellow)
                            Text("Custom Label")
                        }
                    }
                }
                
                Section("Navigation with Data") {
                    ForEach(1...5, id: \.self) { number in
                        NavigationLink(value: number) {
                            HStack {
                                Image(systemName: "\(number).circle.fill")
                                    .foregroundStyle(.green)
                                Text("Item \(number)")
                            }
                        }
                    }
                }
                
                Section("Nested Navigation") {
                    NavigationLink("Deep Navigation Example") {
                        NestedNavigationView(level: 1)
                    }
                }
            }
            .navigationTitle("NavigationStack Demo")
            .navigationDestination(for: Int.self) { number in
                ItemDetailView(number: number)
            }
        }
    }
}

// MARK: - Detail Views
struct DetailView: View {
    let title: String
    let color: Color
    
    var body: some View {
        ZStack {
            color.opacity(0.2)
                .ignoresSafeArea()
            
            VStack(spacing: 20) {
                Image(systemName: "checkmark.circle.fill")
                    .font(.system(size: 60))
                    .foregroundStyle(color)
                
                Text(title)
                    .font(.title)
                    .fontWeight(.bold)
                
                Text("This is a detail view")
                    .foregroundStyle(.secondary)
                
                NavigationLink("Go Deeper") {
                    DetailView(title: "Nested \(title)", color: color.opacity(0.7))
                }
                .buttonStyle(.borderedProminent)
                .tint(color)
            }
            .padding()
        }
        .navigationTitle(title)
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct ItemDetailView: View {
    let number: Int
    
    var body: some View {
        VStack(spacing: 20) {
            Image(systemName: "\(number).circle.fill")
                .font(.system(size: 100))
                .foregroundStyle(.green)
            
            Text("Item \(number)")
                .font(.largeTitle)
                .fontWeight(.bold)
            
            Text("This view was navigated to using NavigationLink with value")
                .multilineTextAlignment(.center)
                .foregroundStyle(.secondary)
                .padding()
        }
        .navigationTitle("Item \(number)")
    }
}

struct NestedNavigationView: View {
    let level: Int
    
    var body: some View {
        VStack(spacing: 30) {
            Text("Level \(level)")
                .font(.system(size: 60, weight: .bold))
                .foregroundStyle(.blue)
            
            Text("This demonstrates nested navigation")
                .foregroundStyle(.secondary)
            
            if level < 5 {
                NavigationLink("Go to Level \(level + 1)") {
                    NestedNavigationView(level: level + 1)
                }
                .buttonStyle(.borderedProminent)
            } else {
                Text("Maximum depth reached!")
                    .font(.headline)
                    .foregroundStyle(.green)
            }
        }
        .navigationTitle("Level \(level)")
    }
}

// MARK: - Programmatic Navigation Demo
struct ProgrammaticNavigationDemo: View {
    @State private var path = NavigationPath()
    @State private var selectedDestination: NavigationDestination?
    
    enum NavigationDestination: String, CaseIterable {
        case home = "Home"
        case profile = "Profile"
        case settings = "Settings"
        case about = "About"
    }
    
    var body: some View {
        NavigationStack(path: $path) {
            VStack(spacing: 20) {
                Text("Programmatic Navigation")
                    .font(.title)
                    .fontWeight(.bold)
                
                Text("Current Path Count: \(path.count)")
                    .font(.headline)
                    .foregroundStyle(.secondary)
                
                Divider()
                
                VStack(spacing: 15) {
                    ForEach(NavigationDestination.allCases, id: \.self) { destination in
                        Button {
                            path.append(destination)
                        } label: {
                            HStack {
                                Image(systemName: iconForDestination(destination))
                                Text("Navigate to \(destination.rawValue)")
                            }
                            .frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.bordered)
                    }
                }
                
                Divider()
                
                VStack(spacing: 15) {
                    Button("Add Multiple Destinations") {
                        path.append(NavigationDestination.profile)
                        path.append(NavigationDestination.settings)
                        path.append(NavigationDestination.about)
                    }
                    .buttonStyle(.borderedProminent)
                    
                    Button("Pop to Root") {
                        path.removeLast(path.count)
                    }
                    .buttonStyle(.bordered)
                    .disabled(path.count == 0)
                    
                    Button("Pop One Level") {
                        if path.count > 0 {
                            path.removeLast()
                        }
                    }
                    .buttonStyle(.bordered)
                    .disabled(path.count == 0)
                }
            }
            .padding()
            .navigationTitle("Programmatic")
            .navigationDestination(for: NavigationDestination.self) { destination in
                ProgrammaticDestinationView(destination: destination, path: $path)
            }
        }
    }
    
    private func iconForDestination(_ destination: NavigationDestination) -> String {
        switch destination {
        case .home: return "house.fill"
        case .profile: return "person.fill"
        case .settings: return "gear"
        case .about: return "info.circle"
        }
    }
}

struct ProgrammaticDestinationView: View {
    let destination: ProgrammaticNavigationDemo.NavigationDestination
    @Binding var path: NavigationPath
    
    var body: some View {
        VStack(spacing: 30) {
            Image(systemName: iconForDestination())
                .font(.system(size: 80))
                .foregroundStyle(.blue)
            
            Text(destination.rawValue)
                .font(.largeTitle)
                .fontWeight(.bold)
            
            Text("Path depth: \(path.count)")
                .foregroundStyle(.secondary)
            
            VStack(spacing: 15) {
                Button("Navigate Forward") {
                    if let nextDestination = nextDestination() {
                        path.append(nextDestination)
                    }
                }
                .buttonStyle(.borderedProminent)
                
                Button("Pop to Root") {
                    path.removeLast(path.count)
                }
                .buttonStyle(.bordered)
            }
        }
        .padding()
        .navigationTitle(destination.rawValue)
    }
    
    private func iconForDestination() -> String {
        switch destination {
        case .home: return "house.fill"
        case .profile: return "person.fill"
        case .settings: return "gear"
        case .about: return "info.circle"
        }
    }
    
    private func nextDestination() -> ProgrammaticNavigationDemo.NavigationDestination? {
        let allCases = ProgrammaticNavigationDemo.NavigationDestination.allCases
        guard let currentIndex = allCases.firstIndex(of: destination),
              currentIndex < allCases.count - 1 else {
            return nil
        }
        return allCases[currentIndex + 1]
    }
}

// MARK: - Modals Demo (Sheet & FullScreenCover)
struct ModalsDemo: View {
    @State private var showSheet = false
    @State private var showFullScreenCover = false
    @State private var sheetDetent: PresentationDetent = .medium
    @State private var selectedItem: String?
    
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 25) {
                    // Sheet Demos
                    GroupBox {
                        VStack(spacing: 15) {
                            Text("Sheet Presentations")
                                .font(.headline)
                            
                            Button("Show Basic Sheet") {
                                showSheet = true
                            }
                            .buttonStyle(.borderedProminent)
                            
                            Button("Show Medium Sheet") {
                                sheetDetent = .medium
                                showSheet = true
                            }
                            .buttonStyle(.bordered)
                            
                            Button("Show Large Sheet") {
                                sheetDetent = .large
                                showSheet = true
                            }
                            .buttonStyle(.bordered)
                        }
                        .padding()
                    }
                    
                    // FullScreenCover Demo
                    GroupBox {
                        VStack(spacing: 15) {
                            Text("Full Screen Cover")
                                .font(.headline)
                            
                            Button("Show Full Screen Cover") {
                                showFullScreenCover = true
                            }
                            .buttonStyle(.borderedProminent)
                            .tint(.purple)
                        }
                        .padding()
                    }
                    
                    // Item-based Sheet
                    GroupBox {
                        VStack(spacing: 15) {
                            Text("Item-based Sheets")
                                .font(.headline)
                            
                            ForEach(["Apple", "Banana", "Cherry"], id: \.self) { item in
                                Button("Show \(item) Details") {
                                    selectedItem = item
                                }
                                .buttonStyle(.bordered)
                            }
                        }
                        .padding()
                    }
                }
                .padding()
            }
            .navigationTitle("Sheets & Covers")
            .sheet(isPresented: $showSheet) {
                SheetContentView(detent: $sheetDetent)
                    .presentationDetents([.medium, .large])
                    .presentationDragIndicator(.visible)
            }
            .fullScreenCover(isPresented: $showFullScreenCover) {
                FullScreenCoverView()
            }
            .sheet(item: $selectedItem) { item in
                ItemSheetView(item: item)
            }
        }
    }
}

struct SheetContentView: View {
    @Environment(\.dismiss) var dismiss
    @Binding var detent: PresentationDetent
    
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 20) {
                    Image(systemName: "doc.text.fill")
                        .font(.system(size: 60))
                        .foregroundStyle(.blue)
                    
                    Text("This is a Sheet")
                        .font(.title)
                        .fontWeight(.bold)
                    
                    Text("Sheets are great for presenting temporary content that doesn't require full screen attention.")
                        .multilineTextAlignment(.center)
                        .foregroundStyle(.secondary)
                        .padding()
                    
                    VStack(spacing: 10) {
                        Text("Try dragging me!")
                            .font(.caption)
                            .foregroundStyle(.secondary)
                        
                        Image(systemName: "arrow.down")
                            .font(.caption)
                            .foregroundStyle(.secondary)
                    }
                    
                    Divider()
                        .padding()
                    
                    NavigationLink("Navigate within Sheet") {
                        DetailView(title: "Sheet Navigation", color: .green)
                    }
                    .buttonStyle(.bordered)
                }
                .padding()
            }
            .navigationTitle("Sheet")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button("Done") {
                        dismiss()
                    }
                }
            }
        }
    }
}

struct FullScreenCoverView: View {
    @Environment(\.dismiss) var dismiss
    @State private var progress: Double = 0.0
    
    var body: some View {
        ZStack {
            LinearGradient(
                colors: [.purple, .blue],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .ignoresSafeArea()
            
            VStack(spacing: 30) {
                Spacer()
                
                Image(systemName: "rectangle.expand.vertical")
                    .font(.system(size: 80))
                    .foregroundStyle(.white)
                
                Text("Full Screen Cover")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundStyle(.white)
                
                Text("This covers the entire screen and is perfect for immersive experiences.")
                    .multilineTextAlignment(.center)
                    .foregroundStyle(.white.opacity(0.9))
                    .padding(.horizontal)
                
                Spacer()
                
                Button {
                    dismiss()
                } label: {
                    Label("Close", systemImage: "xmark.circle.fill")
                        .font(.headline)
                        .foregroundStyle(.purple)
                        .padding()
                        .background(.white)
                        .cornerRadius(10)
                }
                
                Spacer()
            }
            .padding()
        }
    }
}

struct ItemSheetView: View {
    let item: String
    @Environment(\.dismiss) var dismiss
    
    var body: some View {
        NavigationStack {
            VStack(spacing: 20) {
                Text(item)
                    .font(.system(size: 60))
                
                Text("Item Details")
                    .font(.title)
                    .fontWeight(.bold)
                
                Text("This sheet was presented using an optional binding with the item '\(item)'")
                    .multilineTextAlignment(.center)
                    .foregroundStyle(.secondary)
                    .padding()
            }
            .navigationTitle(item)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button("Done") {
                        dismiss()
                    }
                }
            }
        }
    }
}

// Make String identifiable for sheet(item:)
extension String: Identifiable {
    public var id: String { self }
}

// MARK: - Popovers Demo
struct PopoversDemo: View {
    @State private var showPopover = false
    @State private var showContextMenu = false
    @State private var showAlert = false
    @State private var showConfirmation = false
    
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 25) {
                    // Popover
                    GroupBox {
                        VStack(spacing: 15) {
                            Text("Popover")
                                .font(.headline)
                            
                            Button("Show Popover") {
                                showPopover = true
                            }
                            .buttonStyle(.borderedProminent)
                            .popover(isPresented: $showPopover) {
                                PopoverContentView()
                            }
                            
                            Text("Popovers appear as small floating panels")
                                .font(.caption)
                                .foregroundStyle(.secondary)
                        }
                        .padding()
                    }
                    
                    // Context Menu
                    GroupBox {
                        VStack(spacing: 15) {
                            Text("Context Menu")
                                .font(.headline)
                            
                            Text("Long press me!")
                                .padding()
                                .background(Color.blue.opacity(0.2))
                                .cornerRadius(8)
                                .contextMenu {
                                    Button {
                                        print("Copy tapped")
                                    } label: {
                                        Label("Copy", systemImage: "doc.on.doc")
                                    }
                                    
                                    Button {
                                        print("Share tapped")
                                    } label: {
                                        Label("Share", systemImage: "square.and.arrow.up")
                                    }
                                    
                                    Divider()
                                    
                                    Button(role: .destructive) {
                                        print("Delete tapped")
                                    } label: {
                                        Label("Delete", systemImage: "trash")
                                    }
                                }
                            
                            Text("Long press to see context menu")
                                .font(.caption)
                                .foregroundStyle(.secondary)
                        }
                        .padding()
                    }
                    
                    // Alert
                    GroupBox {
                        VStack(spacing: 15) {
                            Text("Alert")
                                .font(.headline)
                            
                            Button("Show Alert") {
                                showAlert = true
                            }
                            .buttonStyle(.borderedProminent)
                            .tint(.orange)
                            .alert("Alert Title", isPresented: $showAlert) {
                                Button("Cancel", role: .cancel) { }
                                Button("OK") { }
                            } message: {
                                Text("This is an alert message")
                            }
                        }
                        .padding()
                    }
                    
                    // Confirmation Dialog
                    GroupBox {
                        VStack(spacing: 15) {
                            Text("Confirmation Dialog")
                                .font(.headline)
                            
                            Button("Show Confirmation") {
                                showConfirmation = true
                            }
                            .buttonStyle(.borderedProminent)
                            .tint(.purple)
                            .confirmationDialog(
                                "Choose an option",
                                isPresented: $showConfirmation,
                                titleVisibility: .visible
                            ) {
                                Button("Option 1") { }
                                Button("Option 2") { }
                                Button("Option 3") { }
                                Button("Cancel", role: .cancel) { }
                            } message: {
                                Text("This is a confirmation dialog with multiple options")
                            }
                        }
                        .padding()
                    }
                    
                    // Menu
                    GroupBox {
                        VStack(spacing: 15) {
                            Text("Menu")
                                .font(.headline)
                            
                            Menu {
                                Button {
                                    print("Edit")
                                } label: {
                                    Label("Edit", systemImage: "pencil")
                                }
                                
                                Button {
                                    print("Duplicate")
                                } label: {
                                    Label("Duplicate", systemImage: "plus.square.on.square")
                                }
                                
                                Menu {
                                    Button("Small") { }
                                    Button("Medium") { }
                                    Button("Large") { }
                                } label: {
                                    Label("Size", systemImage: "arrow.up.left.and.arrow.down.right")
                                }
                                
                                Divider()
                                
                                Button(role: .destructive) {
                                    print("Delete")
                                } label: {
                                    Label("Delete", systemImage: "trash")
                                }
                            } label: {
                                Label("Actions", systemImage: "ellipsis.circle")
                            }
                            .buttonStyle(.borderedProminent)
                            .tint(.green)
                            
                            Text("Tap to see menu options")
                                .font(.caption)
                                .foregroundStyle(.secondary)
                        }
                        .padding()
                    }
                }
                .padding()
            }
            .navigationTitle("Popovers & More")
        }
    }
}

struct PopoverContentView: View {
    @Environment(\.dismiss) var dismiss
    
    var body: some View {
        VStack(spacing: 20) {
            Image(systemName: "info.bubble.fill")
                .font(.system(size: 50))
                .foregroundStyle(.blue)
            
            Text("Popover Content")
                .font(.title2)
                .fontWeight(.bold)
            
            Text("This is a popover! It appears as a floating panel on iPad and as a sheet on iPhone.")
                .multilineTextAlignment(.center)
                .foregroundStyle(.secondary)
            
            Button("Close") {
                dismiss()
            }
            .buttonStyle(.bordered)
        }
        .padding()
        .frame(width: 300)
    }
}

#Preview {
    ContentView()
}
