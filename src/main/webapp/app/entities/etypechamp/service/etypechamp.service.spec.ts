import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEtypechamp } from '../etypechamp.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../etypechamp.test-samples';

import { EtypechampService } from './etypechamp.service';

const requireRestSample: IEtypechamp = {
  ...sampleWithRequiredData,
};

describe('Etypechamp Service', () => {
  let service: EtypechampService;
  let httpMock: HttpTestingController;
  let expectedResult: IEtypechamp | IEtypechamp[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EtypechampService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Etypechamp', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const etypechamp = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(etypechamp).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Etypechamp', () => {
      const etypechamp = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(etypechamp).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Etypechamp', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Etypechamp', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Etypechamp', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEtypechampToCollectionIfMissing', () => {
      it('should add a Etypechamp to an empty array', () => {
        const etypechamp: IEtypechamp = sampleWithRequiredData;
        expectedResult = service.addEtypechampToCollectionIfMissing([], etypechamp);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(etypechamp);
      });

      it('should not add a Etypechamp to an array that contains it', () => {
        const etypechamp: IEtypechamp = sampleWithRequiredData;
        const etypechampCollection: IEtypechamp[] = [
          {
            ...etypechamp,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEtypechampToCollectionIfMissing(etypechampCollection, etypechamp);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Etypechamp to an array that doesn't contain it", () => {
        const etypechamp: IEtypechamp = sampleWithRequiredData;
        const etypechampCollection: IEtypechamp[] = [sampleWithPartialData];
        expectedResult = service.addEtypechampToCollectionIfMissing(etypechampCollection, etypechamp);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(etypechamp);
      });

      it('should add only unique Etypechamp to an array', () => {
        const etypechampArray: IEtypechamp[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const etypechampCollection: IEtypechamp[] = [sampleWithRequiredData];
        expectedResult = service.addEtypechampToCollectionIfMissing(etypechampCollection, ...etypechampArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const etypechamp: IEtypechamp = sampleWithRequiredData;
        const etypechamp2: IEtypechamp = sampleWithPartialData;
        expectedResult = service.addEtypechampToCollectionIfMissing([], etypechamp, etypechamp2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(etypechamp);
        expect(expectedResult).toContain(etypechamp2);
      });

      it('should accept null and undefined values', () => {
        const etypechamp: IEtypechamp = sampleWithRequiredData;
        expectedResult = service.addEtypechampToCollectionIfMissing([], null, etypechamp, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(etypechamp);
      });

      it('should return initial array if no Etypechamp is added', () => {
        const etypechampCollection: IEtypechamp[] = [sampleWithRequiredData];
        expectedResult = service.addEtypechampToCollectionIfMissing(etypechampCollection, undefined, null);
        expect(expectedResult).toEqual(etypechampCollection);
      });
    });

    describe('compareEtypechamp', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEtypechamp(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEtypechamp(entity1, entity2);
        const compareResult2 = service.compareEtypechamp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEtypechamp(entity1, entity2);
        const compareResult2 = service.compareEtypechamp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEtypechamp(entity1, entity2);
        const compareResult2 = service.compareEtypechamp(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
