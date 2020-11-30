# Kiribi-IO
Provides classes and interfaces to read and write objects using the Kiribi IO Framework.

### Introduction
Provides classes and interfaces to read and write objects using the Kiribi IO Framework.

### Features
To do

### Overview
Provides classes and interfaces to read and write objects using the Kiribi IO Framework.

##### Encodable Objects
To do

##### VarInput
Interface for reading Encodable objects.

##### VarOutput
Interface for writing Encodable objects.

### Code Example

 	public Class Foo implements Encodeable {
    	public Foo() {}
 
    	public Foo(VarInput in) throws IOException {
          	...
        }
 
        @Override
        public void write(VarOutput out) throws IOException {
          	...
        }
        ...
    }
 
    ....
 
    VarInput in = ...
    Foo foo = in.read(Foo::new);
    
    VarOutput out = ...
    foo.write(out);


### Module Dependencies
##### Exports
* rs.igram.kiribi.io

### Requirements
To do

### Known Issues
To do
